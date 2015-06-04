import webapp2
import MySQLdb
import json
import database_object


class Subcategory(webapp2.RequestHandler):
	def get(self, **kwargs):
		self.response.headers.add_header("Access-Control-Allow-Origin", "*")
		self.response.headers['content-type'] = 'application/json'
		db = database_object.DatabaseObject()
		if 'id' in kwargs:
			db.cursor.execute('SELECT id, name FROM subcategory_table WHERE id= %s', (kwargs['id'],))
			results = db.cursor.fetchall()
			subcategory = results[0]
			returnSubcategory = {"id":subcategory[0], "name":subcategory[1]}
			db.cursor.execute('SELECT businessId FROM subcategory_businesses WHERE subcatId = %s', (kwargs['id'],))
			businessIds = []
			businesses = []
			for row in db.cursor.fetchall():
				businessIds.append(row[0])
			for busId in businessIds:
				db.cursor.execute('SELECT id, name, phoneNumber, streetAddress, city, state, zipCode, hours, webpage FROM businesses_table WHERE id = ' + str (busId))
				for row in db.cursor.fetchall():
					businesses.append({'id':row[0], 'name':row[1], 'phoneNumber':row[2], 'streetAddress':row[3], 'city':row[4], 'state':row[5], 'zipCode':row[6], 'hours':row[7], 'webpage':row[8]})
			returnSubcategory["businesses"] = businesses
			self.response.write(json.dumps(returnSubcategory)+"\n")
		else:
			db.cursor.execute('SELECT id, name FROM subcategory_table')
			subcategories = [];
			for row in db.cursor.fetchall():
				subcategories.append(dict([('id', row[0]), ('name', row[1])]))
			self.response.write(json.dumps(subcategories)+"\n")

	def post(self, **kwargs):
		self.response.headers.add_header("Access-Control-Allow-Origin", "*")
		self.response.headers['content-type'] = 'application/json'
		db = database_object.DatabaseObject()
		if (self.request.headers['content-type'] != 'application/json'):
			self.abort(415, explanation="Requires json.")
		subcategory = json.loads(self.request.body)
		if ('id' in subcategory):
			db.cursor.execute('UPDATE subcategory_table SET name = %s WHERE id = %s', (subcategory['name'], subcategory['id']))
			db.db.commit()
			currentId = subcategory['id']
		else:
			db.cursor.execute('INSERT INTO subcategory_table (name) values (%s)', (subcategory['name'],))
			db.db.commit()
			currentId = db.cursor.lastrowid
			self.response.headers["location"] = "/subcategory/" + str(currentId)
		subcatResponse = {"id":currentId, "name":subcategory["name"]}
		self.response.write(json.dumps(subcatResponse) + "\n")

	def delete(self, **kwargs):
		self.response.headers.add_header("Access-Control-Allow-Origin", "*")
		self.response.headers['content-type'] = 'application/json'
		db = database_object.DatabaseObject()
		if 'id' in kwargs:
			db.cursor.execute('DELETE FROM subcategory_table WHERE id = %s', (kwargs['id'],))
			db.db.commit()