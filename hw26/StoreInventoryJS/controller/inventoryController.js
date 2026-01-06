const Product = require('../model/Product');

// Obtener todos los productos y el valor total de la bodega
exports.getProducts = async (req, res) => {
    try {
        const products = await Product.find();
        // Suma del (precio con IVA * stock) de todos los productos
        const totalValue = products.reduce((acc, p) => acc + (p.salesPrice * p.stock), 0);
        res.json({ products, totalValue });
    } catch (err) {
        res.status(500).json({ error: err.message });
    }
};

// Guardar un nuevo producto
exports.addProduct = async (req, res) => {
    try {
        const newProduct = new Product(req.body);
        await newProduct.save();
        res.status(201).json(newProduct);
    } catch (err) {
        res.status(400).json({ error: err.message });
    }
};

// Actualizar el stock de un producto existente
exports.updateStock = async (req, res) => {
    try {
        const { name, stock } = req.body;
        await Product.findOneAndUpdate({ name }, { stock });
        res.json({ message: "Stock updated successfully" });
    } catch (err) {
        res.status(400).json({ error: err.message });
    }
};

// Eliminar un producto por nombre
exports.deleteProduct = async (req, res) => {
    try {
        await Product.findOneAndDelete({ name: req.params.name });
        res.json({ message: "Product deleted" });
    } catch (err) {
        res.status(400).json({ error: err.message });
    }
};