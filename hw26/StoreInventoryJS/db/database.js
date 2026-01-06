const mongoose = require('mongoose');
require('dotenv').config();

// Conexión a la base de datos usando la URI del archivo .env
const connectDB = async () => {
    try {
        await mongoose.connect(process.env.MONGO_URI);
        console.log("MongoDB connected to StoreDB");
    } catch (error) {
        console.error("Database connection error:", error);
        process.exit(1);
    }
};

module.exports = connectDB;