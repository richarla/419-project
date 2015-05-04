import webapp2
import MySQLdb
import json
import database_object


class SubcategoryBusiness(webapp2.RequestHandler):
	def put(self, **kwargs):
		self.response.headers['content-type'] = 'application/json'
		db = database_object.DatabaseObject()
		db.cursor.execute('INSERT INTO subcategory_businesses(subcatId, businessId) values (%s, %s)', (kwargs['subcatId'], kwargs['businessId'],))
		db.db.commit()

	def delete(self, **kwargs):
		self.response.headers['content-type'] = 'application/json'
		db = database_object.DatabaseObject()
		if 'id' in kwargs:
			db.cursor.execute('DELETE FROM subcategory_businesses WHERE subcatId = %s AND businessId = %s', (kwargs['subcatId'], kwargs['businessId'],))
			db.db.commit()
