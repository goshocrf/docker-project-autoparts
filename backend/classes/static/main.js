const API_BASE = 'http://localhost:8080';

async function isLoggedIn() {
    try {
        const response = await fetch(API_BASE + '/api/auth/authenticate', {
            credentials: 'include'
        });
        if (response.ok) {
            return await response.json(); // user info if logged in
        } else {
            return null;
        }
    } catch (err) {
        console.error(err);
        return null;
    }
}

function updateNavUserStatus() {
    const userStatus = document.getElementById('user-status');
    isLoggedIn().then(user => {
        if (user) {
            userStatus.innerHTML = `Hello, ${user.name} | <a href="#" id="logoutLink">Logout</a>`;
            document.getElementById('logoutLink').addEventListener('click', async () => {
                await fetch(API_BASE + '/api/auth/logout', { method: 'POST', credentials: 'include' });
                window.location.reload();
            });
        } else {
            userStatus.innerHTML = '<a href="login.html">Login</a> | <a href="register.html">Register</a>';
        }
    });
}

// Load list of all parts
async function loadParts() {
    const response = await fetch(API_BASE + '/api/parts');
    const parts = await response.json();
    const ul = document.getElementById('partsList');
    parts.forEach(part => {
        const li = document.createElement('li');
        const a = document.createElement('a');
        a.textContent = part.name + ' (' + part.category + ')';
        a.href = 'part.html?id=' + part.id;
        li.appendChild(a);
        ul.appendChild(li);
    });
}

// Load list of all cars
async function loadCars() {
    const response = await fetch(API_BASE + '/api/cars');
    const cars = await response.json();
    const ul = document.getElementById('carsList');
    cars.forEach(car => {
        const li = document.createElement('li');
        const a = document.createElement('a');
        a.textContent = car.brand + ' ' + car.model + ' (' + car.year + ')';
        a.href = 'car.html?id=' + car.id;
        li.appendChild(a);
        ul.appendChild(li);
    });
}

// Load list of all manufacturers
async function loadManufacturers() {
    const response = await fetch(API_BASE + '/api/manufacturers');
    const mans = await response.json();
    const ul = document.getElementById('mansList');
    mans.forEach(man => {
        const li = document.createElement('li');
        const a = document.createElement('a');
        a.textContent = man.name + ' (' + man.country + ')';
        a.href = 'manufacturer.html?id=' + man.id;
        li.appendChild(a);
        ul.appendChild(li);
    });
}

// Load part detail page
async function loadPartDetail() {
    const params = new URLSearchParams(window.location.search);
    const id = params.get('id');
    if (!id) return;
    const res = await fetch(API_BASE + '/api/parts/' + id);
    if (!res.ok) {
        document.getElementById('partDetails').textContent = 'Part not found';
        return;
    }
    const part = await res.json();
    let details = `<h2>${part.name}</h2>`;
    details += `<p>Code: ${part.code}</p>`;
    details += `<p>Category: ${part.category}</p>`;
    details += `<p>Buy Price: ${part.buyPrice}</p>`;
    details += `<p>Sell Price: ${part.sellPrice}</p>`;
    if (part.supportedCars) {
        details += '<p>Supported Cars:</p><ul>';
        part.supportedCars.forEach(car => {
            details += `<li><a href="car.html?id=${car.id}">${car.brand} ${car.model}</a></li>`;
        });
        details += '</ul>';
    }
    document.getElementById('partDetails').innerHTML = details;
    const btn = document.createElement('button');
    btn.textContent = 'Add to Cart';
    btn.addEventListener('click', async () => {
        const user = await isLoggedIn();
        if (!user) {
            alert('Please log in to add to cart');
            return;
        }
        addToCart(part);
    });
    document.getElementById('partDetails').appendChild(btn);
}

// Load car detail page
async function loadCarDetail() {
    const params = new URLSearchParams(window.location.search);
    const id = params.get('id');
    if (!id) return;
    const res = await fetch(API_BASE + '/api/cars/' + id);
    if (!res.ok) {
        document.getElementById('carDetails').textContent = 'Car not found';
        return;
    }
    const car = await res.json();
    let details = `<h2>${car.brand} ${car.model} (${car.year})</h2>`;
    if (car.manufacturer) {
        details += `<p>Manufacturer: <a href="manufacturer.html?id=${car.manufacturer.id}">${car.manufacturer.name}</a></p>`;
    }
    if (car.availableParts) {
        details += '<p>Available Parts:</p><ul>';
        car.availableParts.forEach(part => {
            details += `<li><a href="part.html?id=${part.id}">${part.name}</a></li>`;
        });
        details += '</ul>';
    }
    document.getElementById('carDetails').innerHTML = details;
}

// Load manufacturer detail page
async function loadManufacturerDetail() {
    const params = new URLSearchParams(window.location.search);
    const id = params.get('id');
    if (!id) return;
    const res = await fetch(API_BASE + '/api/manufacturers/' + id);
    if (!res.ok) {
        document.getElementById('manDetails').textContent = 'Manufacturer not found';
        return;
    }
    const man = await res.json();
    let details = `<h2>${man.name}</h2>`;
    details += `<p>Country: ${man.country}</p>`;
    details += `<p>Address: ${man.address}</p>`;
    details += `<p>Phone: ${man.phone}</p>`;
    details += `<p>Fax: ${man.fax}</p>`;
    if (man.cars) {
        details += '<p>Cars:</p><ul>';
        man.cars.forEach(car => {
            details += `<li><a href="car.html?id=${car.id}">${car.brand} ${car.model}</a></li>`;
        });
        details += '</ul>';
    }
    // Compile all distinct parts from this manufacturer's cars
    if (man.cars) {
        let parts = [];
        man.cars.forEach(car => {
            if (car.availableParts) {
                car.availableParts.forEach(part => {
                    if (!parts.find(p => p.id === part.id)) {
                        parts.push(part);
                    }
                });
            }
        });
        if (parts.length) {
            details += '<p>Parts:</p><ul>';
            parts.forEach(part => {
                details += `<li><a href="part.html?id=${part.id}">${part.name}</a></li>`;
            });
            details += '</ul>';
        }
    }
    document.getElementById('manDetails').innerHTML = details;
}

// Cart utilities
function getCart() {
    return JSON.parse(localStorage.getItem('cart') || '[]');
}
function saveCart(cart) {
    localStorage.setItem('cart', JSON.stringify(cart));
}
function addToCart(part) {
    let cart = getCart();
    const exists = cart.find(item => item.id === part.id);
    if (exists) {
        exists.qty++;
    } else {
        cart.push({ id: part.id, name: part.name, price: part.sellPrice, qty: 1 });
    }
    saveCart(cart);
    alert('Added to cart');
}
function loadCart() {
    const cart = getCart();
    const tbody = document.getElementById('cartBody');
    tbody.innerHTML = '';
    cart.forEach((item, index) => {
        const row = document.createElement('tr');
        row.innerHTML = `<td>${item.name}</td><td>${item.price}</td><td>${item.qty}</td><td>${item.price * item.qty}</td>
            <td><button onclick="removeFromCart(${index})">Remove</button></td>`;
        tbody.appendChild(row);
    });
    document.getElementById('cartTotal').textContent = cart.reduce((sum, item) => sum + item.price * item.qty, 0);
}
function removeFromCart(index) {
    let cart = getCart();
    cart.splice(index, 1);
    saveCart(cart);
    loadCart();
}

// On page load, decide which data to fetch and update nav
window.onload = () => {
    updateNavUserStatus();
    if (document.getElementById('partsList')) loadParts();
    if (document.getElementById('carsList')) loadCars();
    if (document.getElementById('mansList')) loadManufacturers();
    if (document.getElementById('partDetails')) loadPartDetail();
    if (document.getElementById('carDetails')) loadCarDetail();
    if (document.getElementById('manDetails')) loadManufacturerDetail();
    if (document.getElementById('cartBody')) {
        isLoggedIn().then(user => {
            if (user) {
                loadCart();
            } else {
                document.getElementById('cartContainer').innerHTML = 'Please <a href="login.html">login</a> to view cart.';
            }
        });
    }
};
