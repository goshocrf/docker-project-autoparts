async function loadPart() {
    const params = new URLSearchParams(location.search);
    const id = params.get('id');
    const res = await fetch(`/api/parts/${id}`);
    const part = await res.json();
    document.getElementById('partName').textContent = part.name;
    document.getElementById('partCat').textContent = `Category: ${part.category}`;
    document.getElementById('partDesc').textContent = part.description;
    const ul = document.getElementById('compatibleCars');
    part.compatibleCars.forEach(car => {
        const li = document.createElement('li');
        li.innerHTML = `<a href="car.html?id=${car.id}">${car.name}</a>`;
        ul.appendChild(li);
    });

    if (localStorage.getItem('token')) {
        document.getElementById('addToCartBtn').style.display = 'inline';
        document.getElementById('addToCartBtn').onclick = () => addToCart(part.id);
    }
}

loadPart();
