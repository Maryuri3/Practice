import tkinter as tk
from tkinter import ttk, messagebox
from controller.inventory_controller import InventoryController

class InventoryView(tk.Tk):
    def __init__(self):
        super().__init__()
        self.title("INVENTORY CONTROL AND FINANCIAL ANALYSIS")
        self.geometry("700x600")
        self.configure(bg="#DFE3EE") # Color de fondo original
        self.controller = InventoryController()
        self.init_components()
        self.refresh_table_data()

    def init_components(self):
        # Labels and Entry fields (JTextFields en Java)
        tk.Label(self, text="PRODUCT NAME:", bg="#DFE3EE", font=("Arial", 9, "bold")).place(x=50, y=30)
        self.ent_name = tk.Entry(self)
        self.ent_name.place(x=180, y=30, width=250)

        tk.Label(self, text="BASE PRICE:", bg="#DFE3EE", font=("Arial", 9, "bold")).place(x=50, y=65)
        self.ent_price = tk.Entry(self)
        self.ent_price.place(x=180, y=65, width=120)

        tk.Label(self, text="STOCK UNITS:", bg="#DFE3EE", font=("Arial", 9, "bold")).place(x=50, y=100)
        self.ent_stock = tk.Entry(self)
        self.ent_stock.place(x=180, y=100, width=120)

        # Buttons (JButtons en Java)
        tk.Button(self, text="Save Product", bg="#99FF99", font=("Arial", 9, "bold"), command=self.handle_save).place(x=100, y=150, width=120)
        tk.Button(self, text="Delete Product", bg="#99FF99", font=("Arial", 9, "bold"), command=self.handle_delete).place(x=280, y=150, width=120)
        tk.Button(self, text="Update Stock", bg="#99FF99", font=("Arial", 9, "bold"), command=self.handle_update).place(x=460, y=150, width=120)

        # Data Table (JTable en Java)
        self.tree = ttk.Treeview(self, columns=("Name", "Stock", "Price"), show="headings")
        self.tree.heading("Name", text="Product Name")
        self.tree.heading("Stock", text="Stock Units")
        self.tree.heading("Price", text="Sales Price (Tax)")
        self.tree.place(x=50, y=210, width=600, height=280)
        
        # Evento para seleccionar fila
        self.tree.bind("<<TreeviewSelect>>", self.on_row_select)

        # Total Label
        self.lbl_total = tk.Label(self, text="Total Warehouse Value: $0.00", bg="#DFE3EE", fg="red", font=("Arial", 12, "bold"))
        self.lbl_total.place(x=50, y=520)

    def refresh_table_data(self):
        for item in self.tree.get_children(): self.tree.delete(item)
        data, total = self.controller.get_inventory_data()
        for p in data:
            self.tree.insert("", "end", values=(p['name'], p['stock'], f"${p['salesPrice']:.2f}"))
        self.lbl_total.config(text=f"Total Warehouse Value: ${total:.2f}")

    def on_row_select(self, event):
        # Carga datos de la tabla a los campos de texto
        selected_item = self.tree.focus()
        if selected_item:
            values = self.tree.item(selected_item, 'values')
            self.ent_name.delete(0, tk.END); self.ent_name.insert(0, values[0])
            self.ent_stock.delete(0, tk.END); self.ent_stock.insert(0, values[1])

    def handle_save(self):
        try:
            name = self.ent_name.get()
            price = float(self.ent_price.get())
            stock = int(self.ent_stock.get())
            if price > 0 and stock >= 0:
                self.controller.save_product(name, price, stock)
                self.refresh_table_data()
                messagebox.showinfo("Success", "Product stored in cloud.")
            else: messagebox.showwarning("Warning", "Check values (Price > 0, Stock >= 0)")
        except: messagebox.showerror("Error", "Check your numeric inputs.")

    def handle_delete(self):
        name = self.ent_name.get()
        if messagebox.askyesno("Confirm", f"Delete {name}?"):
            self.controller.delete_product_by_name(name)
            self.refresh_table_data()

    def handle_update(self):
        try:
            self.controller.update_product_stock(self.ent_name.get(), self.ent_stock.get())
            self.refresh_table_data()
            messagebox.showinfo("Success", "Stock updated.")
        except: messagebox.showerror("Error", "Update failed.")