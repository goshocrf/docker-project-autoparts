let allParts = [];

async function loadParts() {
    const res = await fetch('/api/parts');
    allParts = await res.json();
    displayParts(allParts);
}

function displayParts(parts) {
    const tbody = document.querySelector('#partsTable tbody');
    tbody.innerHTML = '';
    parts.forEach(part => {
        const row = document.createElement('tr');
        row.innerHTML = `
      <td>${part.name}</td>
      <td>${part.category}</td>
      <td>${part.manufacturerName}</td>
      <td>${part.compatibleCars.join(', ')}</td>
      <td><button data-id="${part.id}" class="viewBtn">View</button></td>`;
        tbody.appendChild(row);
    });
}

document.getElementById('searchBtn').addEventListener('click', () => {
    const name = document.getElementById('searchName').value.toLowerCase();
    const category = document.getElementById('searchCategory').value.toLowerCase();
    const car = document.getElementById('searchCar').value.toLowerCase();
    const filtered = allParts.filter(p =>
        (!name || p.name.toLowerCase().includes(name)) &&
        (!category || p.category.toLowerCase().includes(category)) &&
        (!car || p.compatibleCars.some(c => c.toLowerCase().includes(car)))
    );
    displayParts(filtered);
});
loadParts();
