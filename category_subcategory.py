import webapp2
import MySQLdb
import json
import database_object


class CategorySubcategory(webapp2.RequestHandler):
	def put(self, **kwargs):
		self.response.headers['content-type'] = 'application/json'
		db = database_object.DatabaseObject()
		db.cursor.execute('INSERT INTO category_subcategory(catId, subcatId) values (%s, %s)', (kwargs['catId'], kwargs['subcatId'],))
		db.db.commit()


	def delete(self, **kwargs):
		self.response.headers['content-type'] = 'application/json'
		db = database_object.DatabaseObject()
		if 'id' in kwargs:
			db.cursor.execute('DELETE FROM category_subcategory WHERE catId = %s AND subcatId = %s', (kwargs['catId'], kwargs['subcatId'],))
			db.db.commit()