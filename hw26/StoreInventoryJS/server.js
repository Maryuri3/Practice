const express = require('express');
const path = require('path');
const connectDB = require('./db/database');
const controller = require('./controller/inventoryController');

const app = express();
app.use(express.json());

// Conexión a la DB al arrancar el servidor
connectDB();

// Servir la interfaz HTML desde la carpeta view
app.get('/', (req, res) => {
    res.sendFile(path.join(__dirname, 'view/index.html'));
});

// Rutas de la API (Endpoints)
app.get('/api/products', controller.getProducts);
app.post('/api/products', controller.addProduct);
app.put('/api/products', controller.updateStock);
app.delete('/api/products/:name', controller.deleteProduct);

const PORT = 3000; // Cambia 3000 por cualquier otro número para un nuevo programa
app.listen(PORT, () => console.log(`Server running on http://localhost:${PORT}`));

/*Instrucciones para que ejecutar:
-En VS Code, abre la terminal y escribe: npm install express mongoose dotenv. (En la terminal de VS Code, busca el ícono de la flecha hacia abajo v junto al símbolo +.

Selecciona Command Prompt (o Símbolo del sistema).
Abrir la terminal en la carpeta y ejecutar npm install.
Vuelve a intentar el comando: npm install express mongoose dotenv.)

-Asegúrate de tener tu archivo .env con la URI de tu base de datos.

Escribe node server.js.

-Abre Chrome y entra a http://localhost:3000
*/