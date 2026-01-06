const mongoose = require('mongoose');

// Definición del esquema con los campos originales de tu proyecto Java
const ProductSchema = new mongoose.Schema({
    name: { type: String, required: true },
    basePrice: { type: Number, required: true },
    stock: { type: Number, required: true }
});

// Regla de negocio: Precio de venta calculado con el 15% de IVA
ProductSchema.virtual('salesPrice').get(function() {
    return this.basePrice * 1.15;
});

ProductSchema.set('toJSON', { virtuals: true });

module.exports = mongoose.model('Product', ProductSchema, 'Inventory');