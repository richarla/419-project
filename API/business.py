import webapp2
import MySQLdb
import json
import database_object
from google.appengine.api import urlfetch
import logging


class Business(webapp2.RequestHandler):
	def get(self, **kwargs):
		self.response.headers.add_header("Access-Control-Allow-Origin", "*")
		self.response.headers['content-type'] = 'application/json'
		db = database_object.DatabaseObject()
		if 'id' in kwargs:
			db.cursor.execute('SELECT id, name, phoneNumber, streetAddress, city, state, zipCode, hours, webpage, latitude, longitude FROM businesses_table WHERE id = %s', (kwargs['id'],))
			results = db.cursor.fetchall()
			if (len(results) ==0):
				self.abort(404, explanation = "Business not found.")
			row = results[0]
			business = {'id':row[0], 'name':row[1], 'phoneNumber':row[2], 'streetAddress':row[3], 'city':row[4], 'state':row[5], 'zipCode':row[6], 'hours':row[7], 'webpage':row[8]}
			if row[9] and row[10]:
				business["latitude"] = row[9]
				business["longitude"] = row[10]
			else:
				self.getGeo(business, db)					
			self.response.write(json.dumps(business)+"\n")
		else:
			db.cursor.execute('SELECT id, name, phoneNumber, streetAddress, city, state, zipCode, hours, webpage, latitude, longitude FROM businesses_table')
			businesses = [];
			for row in db.cursor.fetchall():
				business = {'id':row[0], 'name':row[1], 'phoneNumber':row[2], 'streetAddress':row[3], 'city':row[4], 'state':row[5], 'zipCode':row[6], 'hours':row[7], 'webpage':row[8]}
				if row[9] and row[10]:
					business["latitude"] = row[9]
					business["longitude"] = row[10]
				else:
					self.getGeo(business, db)	
				businesses.append(business)
			self.response.write(json.dumps(businesses)+"\n")

	def getGeo(self, business, db):
		logging.info("Calling Geocode API")
		if business["streetAddress"]:
			url = "https://maps.googleapis.com/maps/api/geocode/json?key=AIzaSyAXA0OXXVRqjaCny-VrBxXDe0WNsNW39Lg&address=" + business["streetAddress"] + ",+" + business["city"] + ",+" + business["state"] 
			if business["zipCode"]:
				url = url + ",+" + business["zipCode"]
			url = url.replace(" ", "+")
			try:
				logging.info("url string:%s", url)
				result = urlfetch.fetch(url)
				if result.status_code == 200:
					resultJson = json.loads(result.content)
					if len(resultJson["results"]) > 0: 
						latitude = resultJson["results"][0]["geometry"]["location"]["lat"]
						longitude = resultJson["results"][0]["geometry"]["location"]["lng"]
						db.cursor.execute('UPDATE businesses_table SET latitude = %s, longitude = %s', (latitude, longitude))
						db.db.commit()
						business["latitude"] = latitude
						business["longitude"] = longitude
					else:
						logging.info("No results")
				else:
					logging.info("Geocode API returned error. content: %s status code:%d", result.content, result.status_code)
			except Exception as e:
				logging.info("Exception: %s", e)

		
	def post(self, **kwargs):
		self.response.headers.add_header("Access-Control-Allow-Origin", "*")
		self.response.headers['content-type'] = 'application/json'
		db = database_object.DatabaseObject()
		if (self.request.headers['content-type'] != 'application/json'):
			self.abort(415, explanation="Requires json.")
		business = json.loads(self.request.body)
		if ('id' in business):
			queryString = "UPDATE businesses_table SET "
			argList= []
			if ('name' in business):
				queryString = queryString + "name = %s,"
				argList.append(business['name'])
			if ('phoneNumber' in business):
				queryString = queryString + "phoneNumber = %s,"
				argList.append(business['phoneNumber'])
			if ('streetAddress' in business):
				queryString = queryString + "streetAddress = %s,"
				argList.append(business['streetAddress'])
			if ('city' in business):
				queryString = queryString + "city = %s,"
				argList.append(business['city'])
			if ('state' in business):
				queryString = queryString + "state = %s,"
				argList.append(business['state'])
			if ('zipCode' in business):
				queryString = queryString + "zipCode = %s,"
				argList.append(business['zipCode'])
			if ('hours' in business):
				queryString = queryString + "hours = %s,"
				argList.append(business['hours'])
			if ('webpage' in business):
				queryString = queryString + "webpage = %s,"
				argList.append(business['webpage'])
			if (len(argList) == 0):
				self.abort(400, explanation="Nothing passed in to update.")
			queryString = queryString[:-1]
			queryString = queryString + " WHERE id = %s"
			argList.append(business['id'])
			db.cursor.execute(queryString, tuple(argList))
			db.db.commit()
			currentId = business['id']
		else:
			db.cursor.execute('INSERT INTO businesses_table (name, phoneNumber, streetAddress, city, state, zipCode, hours, webpage) values (%s, %s, %s, %s, %s, %s, %s, %s)', 
				(business['name'], business['phoneNumber'], business['streetAddress'], business['city'], business['state'], business['zipCode'], business['hours'], business['webpage']))
			db.db.commit()
			currentId = db.cursor.lastrowid
			self.response.headers["location"] = "/business/" + str(currentId)
		businessResponse = {"id":currentId}
		self.response.write(json.dumps(businessResponse) + "\n")


	def delete(self, **kwargs):
		self.response.headers.add_header("Access-Control-Allow-Origin", "*")
		self.response.headers['content-type'] = 'application/json'
		db = database_object.DatabaseObject()
		if 'id' in kwargs:
			db.cursor.execute('DELETE FROM businesses_table WHERE id = %s', (kwargs['id'],))
			db.db.commit()	