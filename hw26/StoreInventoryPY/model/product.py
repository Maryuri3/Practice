class Product:
    def __init__(self, name, base_price, stock):
        self.name = name
        self.base_price = float(base_price)
        self.stock = int(stock)
        self.tax_rate = 0.15  # Regla de negocio: 15% de IVA

    def get_sales_price(self):
        # Calcula el precio final con impuesto
        return self.base_price * (1 + self.tax_rate)

    def to_dict(self):
        # Convierte el objeto a diccionario para MongoDB
        return {
            "name": self.name,
            "basePrice": self.base_price,
            "stock": self.stock
        }