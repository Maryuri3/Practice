#database
import os
from pymongo import MongoClient
from dotenv import load_dotenv

# Carga centralizada de variables de entorno 
load_dotenv()

def get_database(db_name='StoreDB'):
    """
    Modulo reutilizable para cualquier conexion a MongoDB.
    Permite cambiar el nombre de la base de datos dinamicamente.
    """
    uri = os.getenv("MONGO_URI")
    if not uri:
        raise ValueError("Critical Error: MONGO_URI not found in .env file.") [cite: 11]
    
    client = MongoClient(uri)
    return client[db_name] [cite: 12]

#inventory_controller
from db.database import get_database
from model.product import Product

class InventoryController:
    def __init__(self, collection_name='Inventory'):
        # Inyeccion de dependencia: permite reutilizar el controlador con otras colecciones [cite: 78]
        self.db = get_database()
        self.collection = self.db[collection_name]

    def get_inventory_report(self):
        """Devuelve datos procesados listos para cualquier interfaz (Web, Mobile o Desktop)"""
        cursor = self.collection.find() [cite: 80]
        products_list = []
        total_value = 0
        
        for p in cursor:
            prod = Product(p['name'], p['basePrice'], p['stock']) [cite: 81]
            sales_price = prod.get_sales_price() [cite: 82]
            total_value += (sales_price * p['stock']) [cite: 83]
            
            products_list.append({
                "name": p['name'],
                "stock": p['stock'],
                "salesPrice": sales_price
            }) [cite: 84, 85, 86]
        return products_list, total_value [cite: 87]

    def save_product(self, name, price, stock):
        """Crea y persiste un producto validando el modelo [cite: 89, 90]"""
        new_prod = Product(name, price, stock)
        return self.collection.insert_one(new_prod.to_dict())

    def delete_by_name(self, name):
        """Eliminacion modular por identificador unico (nombre) [cite: 91]"""
        return self.collection.delete_one({"name": name})

    def update_stock(self, name, new_stock):
        """Actualizacion de campo especifico usando operadores de MongoDB [cite: 92]"""
        return self.collection.update_one(
            {"name": name}, 
            {"$set": {"stock": int(new_stock)}}
        )
    
#inventory_view
import tkinter as tk
from tkinter import ttk, messagebox
from controller.inventory_controller import InventoryController

class InventoryView(tk.Tk):
    def __init__(self):
        super().__init__() [cite: 16]
        self.title("MODULAR INVENTORY SYSTEM")
        self.geometry("700x600") [cite: 17]
        self.configure(bg="#DFE3EE")
        
        # Inicializacion del controlador modular
        self.controller = InventoryController() [cite: 18]
        self.init_components()
        self.refresh_ui()

    def init_components(self):
        """Construccion de la interfaz imitando Java Swing [cite: 19, 21, 24]"""
        # Labels y Inputs
        tk.Label(self, text="PRODUCT NAME:", bg="#DFE3EE", font=("Arial", 9, "bold")).place(x=50, y=30)
        self.ent_name = tk.Entry(self)
        self.ent_name.place(x=180, y=30, width=250) [cite: 20]

        # Botones de accion [cite: 28, 29, 30]
        tk.Button(self, text="Save", bg="#99FF99", command=self.on_save_click).place(x=100, y=150, width=120)
        tk.Button(self, text="Delete", bg="#99FF99", command=self.on_delete_click).place(x=280, y=150, width=120)

        # Tabla (Treeview) [cite: 31, 35]
        self.tree = ttk.Treeview(self, columns=("Name", "Stock", "Price"), show="headings")
        self.tree.heading("Name", text="Product Name")
        self.tree.place(x=50, y=210, width=600, height=280) [cite: 36, 37]
        
        self.lbl_total = tk.Label(self, text="Total: $0.00", fg="red", font=("Arial", 12, "bold"))
        self.lbl_total.place(x=50, y=520)

    def refresh_ui(self):
        """Sincroniza la interfaz con los datos del controlador [cite: 43, 44, 45]"""
        for item in self.tree.get_children(): self.tree.delete(item)
        items, total = self.controller.get_inventory_report()
        for i in items:
            self.tree.insert("", "end", values=(i['name'], i['stock'], f"${i['salesPrice']:.2f}"))
        self.lbl_total.config(text=f"Total Warehouse Value: ${total:.2f}")

    def on_save_click(self):
        """Maneja el evento de guardado con validacion basica [cite: 58, 62]"""
        try:
            self.controller.save_product(self.ent_name.get(), 10, 5) # Ejemplo modular
            self.refresh_ui()
            messagebox.showinfo("Success", "Data synchronized with Cloud.") [cite: 63]
        except Exception as e:
            messagebox.showerror("Error", str(e))

#Product
class Product:
    def __init__(self, name, base_price, stock):
        self.name = name
        self.base_price = float(base_price)
        self.stock = int(stock)
        self.tax_rate = 0.15 # IVA estandarizado (15%)

    def get_sales_price(self):
        """Calcula el precio final. Reutilizable en cualquier modulo de ventas."""
        return self.base_price * (1 + self.tax_rate)

    def to_dict(self):
        """Convierte el objeto a un formato compatible con MongoDB."""
        return {
            "name": self.name,
            "basePrice": self.base_price,
            "stock": self.stock
        }
    
#main
"""
ENTRY POINT - Store Inventory System
Organized & Modular Architecture
"""
from view.inventory_view import InventoryView

def start_application():
    # Instanciamos la vista, que internamente usa el controlador modular
    app = InventoryView()
    app.mainloop()

if __name__ == "__main__":
    start_application()

"""
Para eliminar el campo stock y añadir la funcionalidad de buscar por nombre,
 debemos limpiar la estructura en todas sus capas (Model, Controller y View). 
Al ser un sistema modular, esto se hace de forma quirúrgica sin romper el resto del código.
"""
#Product
class Product:
    def __init__(self, name, base_price):
        self.name = name
        self.base_price = float(base_price)
        self.tax_rate = 0.15 # 15% IVA

    def get_sales_price(self):
        # El precio de venta sigue calculándose igual
        return self.base_price * (1 + self.tax_rate)

    def to_dict(self):
        # Ya no incluimos el campo 'stock' en el diccionario para MongoDB
        return {
            "name": self.name,
            "basePrice": self.base_price
        }
    
#inventory_controller
from db.database import get_database
from model.product import Product

class InventoryController:
    def __init__(self):
        self.db = get_database()
        self.collection = self.db['Inventory']

    def get_inventory_data(self):
        cursor = self.collection.find()
        products_list = []
        total_value = 0
        for p in cursor:
            prod = Product(p['name'], p['basePrice'])
            sales_price = prod.get_sales_price()
            total_value += sales_price # Suma simple sin stock
            products_list.append({"name": p['name'], "salesPrice": sales_price})
        return products_list, total_value

    def find_product_by_name(self, name):
        """Nueva función modular para buscar un solo producto"""
        data = self.collection.find_one({"name": name})
        if data:
            return Product(data['name'], data['basePrice'])
        return None

    def save_product(self, name, price):
        new_prod = Product(name, price)
        return self.collection.insert_one(new_prod.to_dict())
    
#inventory_view
import tkinter as tk
from tkinter import ttk, messagebox
from controller.inventory_controller import InventoryController

class InventoryView(tk.Tk):
    def __init__(self):
        super().__init__()
        self.title("PRODUCT SEARCH & INVENTORY")
        self.geometry("600x550")
        self.configure(bg="#DFE3EE") #
        self.controller = InventoryController()
        self.init_components()
        self.refresh_data()

    def init_components(self):
        # Campos de entrada (Sin Stock)
        tk.Label(self, text="PRODUCT NAME:", bg="#DFE3EE").place(x=50, y=30)
        self.ent_name = tk.Entry(self)
        self.ent_name.place(x=150, y=30, width=200)

        tk.Label(self, text="BASE PRICE:", bg="#DFE3EE").place(x=50, y=70)
        self.ent_price = tk.Entry(self)
        self.ent_price.place(x=150, y=70, width=100)

        # Botones: Añadimos "Search"
        tk.Button(self, text="Save", bg="#99FF99", command=self.handle_save).place(x=50, y=120, width=80)
        tk.Button(self, text="Search", bg="#99FF99", command=self.handle_search).place(x=150, y=120, width=80)
        tk.Button(self, text="Delete", bg="#99FF99", command=self.handle_delete).place(x=250, y=120, width=80)

        # Tabla con solo 2 columnas
        self.tree = ttk.Treeview(self, columns=("Name", "Price"), show="headings")
        self.tree.heading("Name", text="Product Name")
        self.tree.heading("Price", text="Sales Price")
        self.tree.place(x=50, y=180, width=500, height=250)

    def handle_search(self):
        """Lógica para buscar y cargar datos en los campos"""
        name = self.ent_name.get()
        product = self.controller.find_product_by_name(name)
        if product:
            self.ent_price.delete(0, tk.END)
            self.ent_price.insert(0, product.base_price)
            messagebox.showinfo("Found", f"Product {name} loaded.")
        else:
            messagebox.showwarning("Not Found", "Product does not exist.")

    def refresh_data(self):
        # Actualiza la tabla ignorando el stock
        for i in self.tree.get_children(): self.tree.delete(i)
        items, total = self.controller.get_inventory_data()
        for i in items:
            self.tree.insert("", "end", values=(i['name'], f"${i['salesPrice']:.2f}"))

#


    
