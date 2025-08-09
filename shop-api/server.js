import express from 'express';
import cors from 'cors';

const app = express();
app.use(cors());
app.use(express.json());

const products = [
  { sku: 'SKU-100', name: 'Wireless Headphones', price: 99.99, stock: 5 },
  { sku: 'SKU-200', name: 'Mechanical Keyboard', price: 129.00, stock: 10 },
  { sku: 'SKU-300', name: 'USB-C Cable', price: 9.99, stock: 100 },
];
let cart = [];

app.get('/products', (_req, res) => res.json(products));
app.get('/cart', (_req, res) => res.json(cart));

app.post('/cart', (req, res) => {
  const { sku, qty } = req.body || {};
  const item = products.find(p => p.sku === sku);
  if (!item) return res.status(404).json({ error: 'not_found' });
  if (!qty || qty <= 0) return res.status(400).json({ error: 'qty_must_be_positive' });
  if (item.stock < qty) return res.status(409).json({ error: 'out_of_stock' });
  cart.push({ sku, qty, price: item.price, name: item.name });
  return res.json({ ok: true, cart });
});

app.post('/checkout', async (_req, res) => {
  const paymentUrl = process.env.PAYMENT_URL || 'http://localhost:8080/payment';
  const shippingUrl = process.env.SHIPPING_URL || 'http://localhost:8080/shipping';
  if (cart.length === 0) return res.status(400).json({ error: 'empty_cart' });

  const pay = await fetch(paymentUrl, { method: 'POST' });
  const ship = await fetch(shippingUrl, { method: 'POST' });
  const payJson = await pay.json();
  const shipJson = await ship.json();

  const total = cart.reduce((s, it) => s + it.price * it.qty, 0);
  const order = { status: 'CONFIRMED', total: Number(total.toFixed(2)), payment: payJson, shipping: shipJson, items: cart };
  cart = []; // clear cart after checkout
  return res.json(order);
});

app.post('/cart/clear', (_req, res) => {
  cart = [];
  res.json({ ok: true });
});

const port = process.env.PORT || 3001;
app.listen(port, () => console.log(`shop-api listening on :${port}`));
