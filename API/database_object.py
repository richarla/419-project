import webapp2
import MySQLdb

class DatabaseObject:
	def __init__(self):
		self.db = MySQLdb.connect(unix_socket='/cloudsql/recycling-app-13:storage', db='app_storage', user='root')
		self.cursor = self.db.cursor()

	def __del__(self):
		self.db.close()
