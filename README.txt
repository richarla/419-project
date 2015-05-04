Database Shema: Can be found on drive under "Updated schema". You can see all of the tables, relationships and attribute names. 

API base url: http://www.recycling-app-13.appspot.com

Endpoints:
1) /category
2) category/<categoryID>    ex. category/5
3) /subcategory
4) subcategory/<subcategoryID>    ex. subcategory/8
5) /business
6) business/<businessID>       ex. business/10
7) /category/<categoryID>/subcategory/<subcategoryID>   ex. category/5/subcategory/8
8) /subcategory/<subcategoryID>/business/<businessID>     ex.subcategory/5/business/10

How to do things:
1) Get all categories: Send a get using endpoint #1.
2) Get all subcategories from a specific category: Send a get using endpoint #2 with the ID number of the category.
3) Get all subcategories: Send a get using endpoint #2.
4) Get all businesses from a specific subcategory: Send a get using endpoint #4 with the ID number of the subcategory. 
5) Get all businesses: Send a get using endpoint #5. 
6) Get all info about a specific business: Send a get using endpoint #6, with the ID number of the business. 
7) Add a category: Send a post with endpoint #1 with json that's like '{"name": "Example category name"}. Name is required. 
8) Add a subcategory: Send a post with endpoint #3 with json that includes {"name": "Example subcategory name"}. Name is required. Then use endpoint # 7 to assign the subcategory to a category using the IDs of the category and subcategory.
9) Add a business: Send a post with endpoint #5 with json that includes {"name": "Example business name", "phoneNumber": "555-555-5555"} etc for name, phoneNumber, streetAddress, city, state, zipCode, hours, webpage. Names is required by database, all other fields are required by API so auto set them to be an empty string if the user doesn't input anything. Then add a business to a subcategory using endpoint #8 with the IDs of the subcategory and the business. 
10) Delete a category: Send a delete using endpoint #2 where the ID is the ID of the category to be deleted. This will cascade through the many-to-many.
11) Delete a subcategory: Send a delete using endpoint #4 where the ID is the ID of the subcategory to be deleted. This will cascade through the many-to-many.
12) Delete a business: Send a delete using endpoint #6 where the ID is the ID of the business to be deleted. This will cascade through the many-to-many.
13) Remove a business from a subcategory: Send a delete using endpoint #8 with the IDs of the subcategory and the business. 
14) Remove a subcategory from a category: Send a delete using endpoint #7 with the IDs of the category and subcategory. 
15) Update/edit a category: Send a post to endpoint #1, where the json is like {"id":1, name": "New updated name"}
16) Update/edit a subcategory: Send a post to endpoint #3, where  the json is like {"id":2, name": "New updated subcategory name"}
17) Update/edit a business #############################################


