//database.js
const mongoose = require('mongoose');
require('dotenv').config();

/**
 * DATABASE MODULE
 * Reutilizable: Solo requiere MONGO_URI en el archivo .env
 */
const connectDB = async () => {
    try {
        // Configuramos opciones de conexión modernas para mayor estabilidad
        const conn = await mongoose.connect(process.env.MONGO_URI);
        console.log(`Cloud Database Ready: ${conn.connection.host}`);
    } catch (error) {
        console.error("Critical Connection Error:", error.message);
        process.exit(1);
    }
};

module.exports = connectDB;

//inventoryController
const Product = require('../model/Product');

/**
 * INVENTORY CONTROLLER
 * Modular: Las funciones son independientes y escalables.
 */

// Centraliza la obtención de datos y el análisis financiero
exports.getInventoryReport = async (req, res) => {
    try {
        const products = await Product.find();
        // Lógica de análisis financiero reutilizable en reportes
        const totalValue = products.reduce((acc, p) => acc + (p.salesPrice * p.stock), 0);
        
        res.status(200).json({ 
            success: true, 
            products, 
            totalValue 
        });
    } catch (err) {
        res.status(500).json({ success: false, message: err.message });
    }
};

// Crea un recurso nuevo de forma estandarizada
exports.saveNewProduct = async (req, res) => {
    try {
        const product = new Product(req.body);
        await product.save();
        res.status(201).json({ success: true, data: product });
    } catch (err) {
        res.status(400).json({ success: false, message: err.message });
    }
};

// Actualiza stock mediante búsqueda por nombre (igual que en Java)
exports.modifyProductStock = async (req, res) => {
    try {
        const { name, stock } = req.body;
        await Product.findOneAndUpdate({ name }, { stock });
        res.status(200).json({ success: true, message: "Stock Updated" });
    } catch (err) {
        res.status(400).json({ success: false, message: err.message });
    }
};

// Elimina recursos basándose en parámetros dinámicos
exports.removeProduct = async (req, res) => {
    try {
        await Product.findOneAndDelete({ name: req.params.name });
        res.status(200).json({ success: true, message: "Product Removed" });
    } catch (err) {
        res.status(400).json({ success: false, message: err.message });
    }
};

//server.js
const express = require('express');
const path = require('path');
const connectDB = require('./db/database');
const inventory = require('./controller/inventoryController');

const app = express();

// MIDDLEWARE: Configuración global reutilizable
app.use(express.json());

// INITIALIZATION: Conexión modular
connectDB();

// VIEW ENGINE: Servir archivos estáticos de forma organizada
app.use(express.static(path.join(__dirname, 'view')));

app.get('/', (req, res) => {
    res.sendFile(path.join(__dirname, 'view/index.html'));
});

/**
 * API ROUTES (Standard REST pattern)
 * Modular: Estas rutas pueden moverse a un archivo routes.js si el proyecto crece.
 */
app.get('/api/products', inventory.getInventoryReport);
app.post('/api/products', inventory.saveNewProduct);
app.put('/api/products', inventory.modifyProductStock);
app.delete('/api/products/:name', inventory.removeProduct);

const PORT = process.env.PORT || 3000;
app.listen(PORT, () => {
    console.log(`System Online: http://localhost:${PORT}`);
});

/*Para quitar el campo stock de tu proyecto, debes realizar cambios en cada capa de tu arquitectura MVC (Model, View, Controller). 
Como tu código es modular, solo tienes que eliminar las referencias a esa variable específica sin afectar el resto del sistema.*/
//Product.js

const mongoose = require('mongoose');

const ProductSchema = new mongoose.Schema({
    name: { type: String, required: true },
    basePrice: { type: Number, required: true }
    // Se elimina el campo 'stock' aquí
});

// Ajuste de lógica: El precio de venta sigue calculándose con el 15% de IVA
ProductSchema.virtual('salesPrice').get(function() {
    return this.basePrice * 1.15;
});

ProductSchema.set('toJSON', { virtuals: true });

module.exports = mongoose.model('Product', ProductSchema, 'Inventory');

//inventoryController
const Product = require('../model/Product');

exports.getProducts = async (req, res) => {
    try {
        const products = await Product.find();
        // Ahora el totalValue es simplemente la suma de los precios individuales
        const totalValue = products.reduce((acc, p) => acc + p.salesPrice, 0);
        res.json({ products, totalValue });
    } catch (err) {
        res.status(500).json({ error: err.message });
    }
};

// Eliminar exports.updateStock completamente del archivo

//index
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Store Inventory Management - Modular</title>
    <style>
        /* Estilos para imitar el look de Java Swing */
        body { font-family: 'Tahoma', sans-serif; background-color: #DFE3EE; padding: 20px; }
        .main-container { 
            background: #F0F0F0; 
            padding: 20px; 
            border: 2px solid #A0A0A0; 
            width: 700px; 
            margin: auto; 
            box-shadow: 5px 5px 15px rgba(0,0,0,0.1);
        }
        h1 { color: black; text-align: center; font-size: 18px; font-weight: bold; text-transform: uppercase; }
        
        .form-group { margin-bottom: 10px; }
        label { display: inline-block; width: 130px; font-weight: bold; font-size: 12px; }
        
        input { padding: 3px; border: 1px solid #7A7A7A; width: 180px; }
        /* Ocultar flechas en campos numéricos para parecerse a JTextField */
        input[type=number]::-webkit-inner-spin-button, 
        input[type=number]::-webkit-outer-spin-button { -webkit-appearance: none; margin: 0; }

        .button-panel { text-align: center; margin: 20px 0; }
        button { 
            background-color: #99FF99; /* Color verde de tus botones en Java */
            border: 1px solid #707070; 
            padding: 6px 18px; 
            margin: 5px; 
            font-weight: bold;
            cursor: pointer; 
        }
        button:hover { background-color: #77EE77; }

        table { width: 100%; border-collapse: collapse; background: white; border: 1px solid #A0A0A0; margin-top: 10px; }
        th { background-color: #E0E0E0; border: 1px solid #A0A0A0; padding: 6px; font-size: 12px; }
        td { border: 1px solid #D0D0D0; padding: 6px; text-align: center; font-size: 12px; cursor: pointer; }
        tr:hover { background-color: #F5F5F5; }

        .total-label { margin-top: 20px; font-weight: bold; font-size: 15px; color: #B22222; }
    </style>
</head>
<body>
    <div class="main-container">
        <h1>INVENTORY CONTROL AND FINANCIAL ANALYSIS</h1>
        
        <div class="form-group">
            <label>PRODUCT NAME:</label>
            <input type="text" id="name">
        </div>
        <div class="form-group">
            <label>BASE PRICE ($):</label>
            <input type="number" id="basePrice" min="0" step="0.01">
        </div>
        <div class="form-group">
            <label>STOCK UNITS:</label>
            <input type="number" id="stock" min="0">
        </div>

        <div class="button-panel">
            <button onclick="handleSave()">Save Product</button>
            <button onclick="handleDelete()">Delete Product</button>
            <button onclick="handleUpdate()">Update Stock</button>
        </div>

        <table>
            <thead>
                <tr>
                    <th>Product Name</th>
                    <th>Stock Units</th>
                    <th>Sales Price (Tax Incl.)</th>
                </tr>
            </thead>
            <tbody id="inventoryTable"></tbody>
        </table>

        <div id="totalValueDisplay" class="total-label">TOTAL WAREHOUSE VALUE: $0.00</div>
    </div>

    <script>
        /**
         * MODULAR UI SCRIPTS
         * Handles interactions with the Node.js API endpoints
         */

        // Initial data load from the API
        async function loadInventory() {
            try {
                const response = await fetch('/api/products');
                const data = await response.json();
                const tableBody = document.getElementById('inventoryTable');
                tableBody.innerHTML = "";
                
                data.products.forEach(product => {
                    const row = document.createElement('tr');
                    
                    // Click event to load data into inputs (Java Swing behavior)
                    row.onclick = () => {
                        document.getElementById('name').value = product.name;
                        document.getElementById('stock').value = product.stock;
                    };

                    row.innerHTML = `
                        <td>${product.name}</td>
                        <td>${product.stock}</td>
                        <td>$${product.salesPrice.toFixed(2)}</td>
                    `;
                    tableBody.appendChild(row);
                });

                // Update financial analysis label
                document.getElementById('totalValueDisplay').innerText = 
                    `TOTAL WAREHOUSE VALUE: $${data.totalValue.toFixed(2)}`;
            } catch (err) {
                console.error("Failed to load inventory:", err);
            }
        }

        // Action: Save new product
        async function handleSave() {
            const name = document.getElementById('name').value.trim();
            const basePrice = parseFloat(document.getElementById('basePrice').value);
            const stock = parseInt(document.getElementById('stock').value);

            // Business logic validation
            if (!name || isNaN(basePrice) || basePrice <= 0 || isNaN(stock) || stock < 0) {
                alert("Validation Error: Please check Product Name, Price (>0) and Stock (>=0).");
                return;
            }

            await fetch('/api/products', {
                method: 'POST',
                headers: {'Content-Type': 'application/json'},
                body: JSON.stringify({ name, basePrice, stock })
            });
            resetForm();
        }

        // Action: Delete selected product by name
        async function handleDelete() {
            const name = document.getElementById('name').value.trim();
            if (!name) return alert("Please select a product from the table first.");
            
            if (confirm(`Are you sure you want to permanently delete '${name}'?`)) {
                await fetch(`/api/products/${name}`, { method: 'DELETE' });
                resetForm();
            }
        }

        // Action: Update units of an existing product
        async function handleUpdate() {
            const name = document.getElementById('name').value.trim();
            const stock = parseInt(document.getElementById('stock').value);

            if (!name || isNaN(stock) || stock < 0) return alert("Please enter a valid Stock value.");

            await fetch('/api/products', {
                method: 'PUT',
                headers: {'Content-Type': 'application/json'},
                body: JSON.stringify({ name, stock })
            });
            resetForm();
        }

        // Helper to clear inputs and refresh UI
        function resetForm() {
            document.getElementById('name').value = "";
            document.getElementById('basePrice').value = "";
            document.getElementById('stock').value = "";
            loadInventory();
        }

        // Initialize table on page load
        window.onload = loadInventory;
    </script>
</body>
</html>