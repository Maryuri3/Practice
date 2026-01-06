from view.inventory_view import InventoryView

if __name__ == "__main__":
    # Arranca la aplicación de escritorio
    app = InventoryView()
    app.mainloop()

"""
INSTRUCCIONES PARA EJECUTAR
1. REQUISITOS PREVIOS:
   - Tener instalado Python 3.12 o superior.
2. INSTALACIÓN DE LIBRERÍAS:
   Abrir la terminal en la carpeta del proyecto y ejecutar:
   pip install pymongo python-dotenv
   python -m pip install pymongo python-dotenv

3. CONFIGURACIÓN DEL ARCHIVO .ENV:
   - Asegurarse de que el archivo '.env' esté en la misma carpeta que este 'main.py'.

4. CONFIGURACIÓN EN VS CODE (Si las librerías aparecen marcadas con error):
   - Presionar Ctrl + Shift + P.
   - Seleccionar 'Python: Select Interpreter'.
   - Elegir la versión de Python donde se instalaron las librerías (ej: Python 3.12).

5. EJECUCIÓN:
   Ejecutar desde la terminal con: python main.py
------------------------------------------------
"""