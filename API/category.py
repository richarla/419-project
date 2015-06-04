import webapp2
import MySQLdb
import json
import database_object


class Category(webapp2.RequestHandler):
	def get(self, **kwargs):
		self.response.headers.add_header("Access-Control-Allow-Origin", "*")
		self.response.headers['content-type'] = 'application/json'
		db = database_object.DatabaseObject()
		if 'id' in kwargs:
			db.cursor.execute('SELECT id, name FROM category_table WHERE id= %s', (kwargs['id'],))
			results = db.cursor.fetchall()
			category = results[0]
			returnCategory = {"id":category[0], "name":category[1]}
			db.cursor.execute('SELECT subcatId FROM category_subcategory WHERE catId = %s', (kwargs['id'],))
			subcategoryIds = []
			subcategories = []
			for row in db.cursor.fetchall():
				subcategoryIds.append(row[0])
			for subcatId in subcategoryIds:
				db.cursor.execute('SELECT id, name FROM subcategory_table WHERE id = ' + str (subcatId))
				for row in db.cursor.fetchall():
					subcategories.append(dict([('id', row[0]), ('name', row[1])]))
			returnCategory["subcategories"] = subcategories
			self.response.write(json.dumps(returnCategory)+"\n")
		else:
			db.cursor.execute('SELECT id, name FROM category_table')
			categories = [];
			for row in db.cursor.fetchall():
				categories.append(dict([('id', row[0]), ('name', row[1])]))
			self.response.write(json.dumps(categories)+"\n")

	def post(self, **kwargs):
		self.response.headers.add_header("Access-Control-Allow-Origin", "*")
		self.response.headers['content-type'] = 'application/json'
		db = database_object.DatabaseObject()
		if (self.request.headers['content-type'] != 'application/json'):
			self.abort(415, explanation="Requires json.")
		category = json.loads(self.request.body)
		if ('id' in category):
			db.cursor.execute('UPDATE category_table SET name = %s WHERE id = %s', (category['name'], category['id']))
			db.db.commit()
			currentId = category['id']
		else:
			db.cursor.execute('INSERT INTO category_table (name) values (%s)', (category['name'],))
			db.db.commit()
			currentId = db.cursor.lastrowid
			self.response.headers["location"] = "/category/" + str(currentId)
		catResponse = {"id":currentId, "name":category["name"]}
		self.response.write(json.dumps(catResponse) + "\n")

	def delete(self, **kwargs):
		self.response.headers.add_header("Access-Control-Allow-Origin", "*")
		self.response.headers['content-type'] = 'application/json'
		db = database_object.DatabaseObject()
		if 'id' in kwargs:
			db.cursor.execute('DELETE FROM category_table WHERE id = %s', (kwargs['id'],))
			db.db.commit()