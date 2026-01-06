import os
from pymongo import MongoClient
from dotenv import load_dotenv

# Esto carga las variables del archivo .env que está en la raíz
load_dotenv()

def get_database():
    # Obtenemos la URI de la nube. 
    # Si 'MONGO_URI' no se carga bien, fallará y buscará 'localhost'
    uri = os.getenv("MONGO_URI")
    
    if not uri:
        print("ERROR: No se encontró la MONGO_URI en el archivo .env")
    
    client = MongoClient(uri)
    return client['StoreDB']