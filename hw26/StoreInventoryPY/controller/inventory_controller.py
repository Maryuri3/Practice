from db.database import get_database
from model.product import Product

class InventoryController:
    def __init__(self):
        self.db = get_database()
        self.collection = self.db['Inventory'] # Colección en MongoDB

    def get_inventory_data(self):
        # Obtener lista de productos y calcular el valor total de bodega
        cursor = self.collection.find()
        products_list = []
        total_warehouse_value = 0
        
        for p in cursor:
            prod = Product(p['name'], p['basePrice'], p['stock'])
            sales_price = prod.get_sales_price()
            total_warehouse_value += (sales_price * p['stock'])
            
            products_list.append({
                "name": p['name'],
                "stock": p['stock'],
                "salesPrice": sales_price
            })
        return products_list, total_warehouse_value

    def save_product(self, name, price, stock):
        # Crear y guardar nuevo producto
        new_prod = Product(name, price, stock)
        return self.collection.insert_one(new_prod.to_dict())

    def delete_product_by_name(self, name):
        # Eliminar por nombre como en Java
        return self.collection.delete_one({"name": name})

    def update_product_stock(self, name, new_stock):
        # Actualizar solo el stock
        return self.collection.update_one({"name": name}, {"$set": {"stock": int(new_stock)}})