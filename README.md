# Food-Order-Management
First of all, you need to install JavaFX (https://www.youtube.com/watch?v=Ope4icw6bVk) and SQLite(https://www.youtube.com/watch?v=wXEZZ2JT3-k).
Don't worry for the creation of the database because it's in DB Resources folder alongside SQLite's jdbc (you have the tutorial to add it).
The database has 4 tables:
1) admins - where are the administrators so you can log as one of them; it has 3 fields (the id of the admin - integer, the name of the admin - text, the password of admin - text)
2) clients - where are the clients so you can log in as one of them; it has 4 fields ( the id of the client - integer, the name of the client - text, the password of the client - text, the total_bill which is the total cost of the orders - numeric)
3) orders - where is's stored every order made and it has 4 fields (the id of the order - integer, the id of the client - integer, the id of the product - integer, the number of ordered product - integer)
4) products - where it's stored every product; it has 4 fields (the id of the product - integer, the name of the product - text, the price of the product - numeric and the number in stock - integer)

Then all you need to do is run the main function in the App class. After that you need to log in and you have the choice to do it as either client or administrator (the name and password must correspond with the ones in the table admins or clients).
1) LOG IN AS CLIENT:
- after you log in as a client you can see all the products listed in the left table
- you can click on one product, choose the number of products you want and after you click the button Add that product is added on your cart (the table on the right side)
- you can remove items from the cart by clicking one product and then press the Remove button
- when you click the the Finish button your order is created in orders table, the informations about all the products you ordered are updated and the also the total_bill of the client is updated
- in the top left corner there is a menu from which you can order the products by name or by price (if you select the "by price" method you can also choose if you want to list the products that have the price lower, greater or equal to the one introduce); if you want to see all the products again simply press the Back button
