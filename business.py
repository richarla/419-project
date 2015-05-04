import webapp2
import MySQLdb
import json
import database_object


class Business(webapp2.RequestHandler):
	def get(self, **kwargs):
		self.response.headers['content-type'] = 'application/json'
		db = database_object.DatabaseObject()
		if 'id' in kwargs:
			db.cursor.execute('SELECT id, name, phoneNumber, streetAddress, city, state, zipCode, hours, webpage FROM businesses_table WHERE id = %s', (kwargs['id'],))
			results = db.cursor.fetchall()
			if (len(results) ==0):
				self.abort(404, explanation = "Business not found.")
			row = results[0]
			business = {'id':row[0], 'name':row[1], 'phoneNumber':row[2], 'streetAddress':row[3], 'city':row[4], 'state':row[5], 'zipCode':row[6], 'hours':row[7], 'webpage':row[8]}					
			self.response.write(json.dumps(business)+"\n")
		else:
			db.cursor.execute('SELECT id, name, phoneNumber, streetAddress, city, state, zipCode, hours, webpage FROM businesses_table')
			businesses = [];
			for row in db.cursor.fetchall():
				businesses.append(dict([('id', row[0]), ('name', row[1]), ('phoneNumber', row[2]), ('streetAddress', row[3]), ('city', row[4]), ('state', row[5]), ('zipCode', row[6]), ('hours', row[7]), ('webpage', row[8])]))
			self.response.write(json.dumps(businesses)+"\n")

	def post(self, **kwargs):
		self.response.headers['content-type'] = 'application/json'
		db = database_object.DatabaseObject()
		if (self.request.headers['content-type'] != 'application/json'):
			self.abort(415, explanation="Requires json.")
		business = json.loads(self.request.body)
		if (business['id']):
			db.cursor.execute('UPDATE business_table SET name = %s WHERE id = %s', (business['name'], business['id']))
			db.db.commit()
			currentId = business['id']
		else:
			db.cursor.execute('INSERT INTO businesses_table (name, phoneNumber, streetAddress, city, state, zipCode, hours, webpage) values (%s, %s, %s, %s, %s, %s, %s, %s)', 
				(business['name'], business['phoneNumber'], business['streetAddress'], business['city'], business['state'], business['zipCode'], business['hours'], business['webpage']))
			db.db.commit()
			currentId = db.cursor.lastrowid
			self.response.headers["location"] = "/business/" + str(currentId)
		businessResponse = {"id":currentId, "name":business["name"]}
		self.response.write(json.dumps(businessResponse) + "\n")


	def delete(self, **kwargs):
		self.response.headers['content-type'] = 'application/json'
		db = database_object.DatabaseObject()
		if 'id' in kwargs:
			db.cursor.execute('DELETE FROM businesses_table WHERE id = %s', (kwargs['id'],))
			db.db.commit()	
