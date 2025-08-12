//MAPA
const map = L.map('map').setView([42.2,-8.6], 15);
//ICONO PERSONALIZADO
const myIcon = L.icon({
    iconUrl: '/images/icons/pin-location.png',
    iconSize: [40, 40],          // puedes ajustar según el tamaño real
    iconAnchor: [20, 40],        // centro inferior
    popupAnchor: [0, -40]        // para que el popup salga sobre el pin
});
//UBICACIÓN DEL USUARIO
let userMarker = null; // marcador global para la "ubicación simulada"
//MOSTRAR RUTA
var currentRoute = null;
//DESTINO DEL BUSCADOR DE SITIOS
let destinoMarker = null;
//BASE SELECCIONADA (int - base.id)
var selectedBase;
//VEHICULO SELECCIONADO (int - vehicle.id)
var selectedVehicle;
//EVENT LISTENER CUANDO CARGA EL DOM
document.addEventListener("DOMContentLoaded", async () => {

    initMap();
    showBaseList();
    initGeoLocation();
    travelSimulator();

})
//INICIALIZAR MAPA DE LEAFLET
function initMap(){
    // MAPA DE LEAFLET
    L.tileLayer('https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png', {
        attribution: '&copy; OpenStreetMap contributors'
    }).addTo(map);
}
//RANDOMIZADOR DE VALORES DE VIAJE
async function vehicleUsageSimulator() {
    // Obtener el vehiculo mediante el id y luego cambiarle los valores
    const basesResponse = await fetch('/api/bases');
    const bases = await basesResponse.json();

    bases.forEach(base => {
        base.vehicles.forEach(vehicle => {
            if (vehicle.vehicleId === selectedVehicle) {
                fetch(`/api/use-vehicle/${selectedVehicle}`, {
                    method: 'PUT'
                }).then(res => {
                    if (!res.ok) {
                        throw new Error('Error al usar vehículo');
                    }
                    return res.json();
                })
                .then(async data => {
                    console.log('Vehículo actualizado:', data);

                    // ➕ Obtener dirección de destino
                    const destino = document.getElementById('destinoInput').value;
                    if (!destino) {
                        alert('Por favor, ingresa un destino válido');
                        return;
                    }

                    const nominatimURL = `https://nominatim.openstreetmap.org/search?format=json&q=${encodeURIComponent(destino)}`;
                    const geoResponse = await fetch(nominatimURL);
                    const geoData = await geoResponse.json();

                    if (geoData.length > 0) {
                        const { lat, lon, display_name } = geoData[0];
                        const nuevaUbicacion = L.latLng(lat, lon);

                        // Mover o crear el marcador de ubicación simulada
                        if (userMarker) {
                            userMarker.setLatLng(nuevaUbicacion).bindPopup(`🫣Ubicación simulada: ${display_name}`).openPopup();
                        } else {
                            userMarker = L.marker(nuevaUbicacion, { icon: L.icon({ iconUrl: 'https://cdn-icons-png.flaticon.com/512/684/684908.png', iconSize: [25, 25] }) })
                                .addTo(map)
                                .bindPopup(`Ubicación simulada: ${display_name}`)
                                .openPopup();
                        }

                        map.setView(nuevaUbicacion, 15);
                    } else {
                        alert('No se encontró la ubicación ingresada.');
                    }
                })
                .catch(error => {
                    console.error('Error al usar vehículo:', error);
                });
            }
        });
    });
}
//SIMULACION DE VIAJE
function travelSimulator(){
    //Escuchamos por evento de click en el botón de SIMULADOR
    document.getElementById('viajarBtn').addEventListener('click', () => {
        document.getElementById('busquedaContainer').classList.remove('hidden');
    });
    //Busca el dentino por el valor ingresado en el input
    document.getElementById('buscarBtn').addEventListener('click', async () => {
    const destino = document.getElementById('destinoInput').value;
    if (!destino) return;

    const url = `https://nominatim.openstreetmap.org/search?format=json&q=${encodeURIComponent(destino)}`;
    const res = await fetch(url);
    const data = await res.json();

    if (data.length > 0) {
        const { lat, lon, display_name } = data[0];

        // Eliminar marcador anterior si existe
        if (destinoMarker) {
        map.removeLayer(destinoMarker);
        }

        // Agregar nuevo marcador
        destinoMarker = L.marker([lat, lon]).addTo(map);
        destinoMarker.bindPopup(`📍 ${display_name}`).openPopup();
        map.setView([lat, lon], 15);
        showDestinationRoute(lat,lon);
    } else {
        alert('Destino no encontrado');
    }
    });
}
//MOSTRAR LISTA DE BASES
async function showBaseList(){
    //FETCH DE LAS BASES
    const basesResponse = await fetch('/api/bases');
    const bases = await basesResponse.json();

    //Variable de la lista de vehiculos
    const list = document.getElementById("base-list");

    bases.forEach(b => {
        //Marcadores del mapa
        const marker = L.marker([b.coords.x, b.coords.y])
            .addTo(map)
            .bindPopup(`<strong>${b.id}</strong>`);

        //Lista lateral de bases
        const li = document.createElement("li");
        li.className = "cursor-pointer hover:text-blue-400";
        li.textContent = `• Base Id: ${b.id}`;
            
        li.addEventListener("click", () => {
            console.log("click en:", b.id);
            map.setView([b.coords.x, b.coords.y], 15);
            marker.openPopup();
            //AQUI TENDRIA QUE PASAR EL LIST DE VEHICLES DE LA BASE
            showBaseOverlay(b.vehicles);
            showRoute(b.coords.x, b.coords.y);
            selectedBase = b.id;
        });

        list.appendChild(li);
    });
}

//MOSTRAR LA RUTA A LA BASE DESDE TU LOCALIZACIÓN ACTUAL
function showRoute(baseLatitude, baseLongitude){
    navigator.geolocation.getCurrentPosition(pos => {
        const origin = L.latLng(pos.coords.latitude, pos.coords.longitude);
        const destination = L.latLng(baseLatitude, baseLongitude);

        if(currentRoute){
            currentRoute.remove();
        }
        currentRoute = L.Routing.control({
            waypoints: [origin, destination],
            routeWhileDragging: false
        }).addTo(map);
    });
}
//MOSTRAR DESTINO DE RUTA
async function showDestinationRoute(lat, lon){
    //FETCH DE LAS BASES
    const basesResponse = await fetch('/api/bases');
    const bases = await basesResponse.json();

    bases.forEach(base => {
        if(base.id === selectedBase){
            const origin = L.latLng(base.coords.x, base.coords.y);
            const destination = L.latLng(lat, lon);

        if(currentRoute){
            currentRoute.remove();
        }
        currentRoute = L.Routing.control({
            waypoints: [origin, destination],
            routeWhileDragging: false
        }).addTo(map);
        }
    });
}
//MOSTRAR ULTIMO ELEMENTO DE LA LISTA DE BASES
async function showLastBase(){
    //Obtener lista de las bases
    const list = document.getElementById('base-list');
    //Fetch de las bases
    const basesResponse = await fetch('/api/bases');
    const bases = await basesResponse.json();
    //Obtenemos la ultima base
    const lastBase = bases[bases.length -1];
    //Creamos un marcador para el mapa con las coordenadas de la ultima base
    const marker = L.marker([lastBase.coords.x, lastBase.coords.y])
                    .addTo(map)
                    .bindPopup(`<strong>${lastBase.id}</strong>`);
    //Creamos el elemento de list item para mostrarlo en la lista
    const li = document.createElement("li");
    li.className = 'cursor-pointer hover:text-blue-400';
    li.textContent = `• Base Id: ${lastBase.id}`;
    li.addEventListener('click', () => {
        map.setView([lastBase.coords.x, lastBase.coords.y], 15);
        marker.openPopup();
        showBaseOverlay(lastBase.vehicles);
    });
    
    //Agregamos el elemento a la lista
    list.appendChild(li);
}
//OVERLAY DE LOS VEHICULOS
//Mostrar Overlay de Vehiculos
function showVehicleOverlay(vehicle) {
    const overlay = document.getElementById('vehicle-info-overlay');
    const vehicleList = document.getElementById('vehicle-info-list');

    vehicleList.innerHTML = '';

    //Mapeo de campos y etiquetas
    const fields = {
        Name: vehicle.name,
        'Breakdown State': vehicle.breakdownState,
        Availability: vehicle.available,
        Battery: vehicle.battery,
        Fee: vehicle.fee
    };

    //Crear y añadir dinámicamente
    for (const [label, value] of Object.entries(fields)) {
        const p = document.createElement('p');
        p.className = 'flex justify-between hover:text-blue-400 w-55';

        const spanLabel = document.createElement('span');
        spanLabel.textContent = `${label}:`;

        const spanValue = document.createElement('span');
        spanValue.textContent = value;

        p.appendChild(spanLabel);
        p.appendChild(spanValue);

        vehicleList.appendChild(p);
    }

    //Mostrar el overlay con transición
    overlay.classList.remove('hidden');

    setTimeout(() => {
        overlay.classList.remove('opacity-0');
        overlay.classList.add('opacity-100');
    }, 10);
}
//Esconder Overlay de Vehiculos
function hideVehicleOverlay() {
  const overlay = document.getElementById('vehicle-info-overlay');

  overlay.classList.remove('opacity-100');
  overlay.classList.add('opacity-0');

  setTimeout(() => {
    overlay.classList.add('hidden');
  }, 500);
}
//OVERLAY DE LAS BASES
//Mostrar Overlay de Bases
function showBaseOverlay(vehicles) {
    const overlay = document.getElementById('base-overlay');
    const vehicleList = document.getElementById('vehicle-list');
    console.log('Vehiculos: ', vehicles);
    //Limpia la lista antes de volver a cargarla
    vehicleList.innerHTML = '';
    vehicles.forEach(v => {
        
        const li = document.createElement('li');
        li.className = 'cursor-pointer hover:text-blue-400';
        li.textContent = `• Vehicle: ${v.name} Id: ${v.vehicleId}`

        li.addEventListener('click', () => {
            //Mostramnos el overlay del vehiculo seleccionado 
            showVehicleOverlay(v);
            //Asignamos el id del vehiculo a la variable 
            selectedVehicle = v.vehicleId;
            console.log('Selected Vehicle: ', selectedVehicle);

        });

        vehicleList.appendChild(li);
    });

    // Asegura que tenga clase opacity-0 para iniciar transición
    overlay.classList.remove('hidden');

    // Activar transición de opacidad
    setTimeout(() => {
        overlay.classList.remove('opacity-0');
        overlay.classList.add('opacity-100');
    }, 10);
}
//Esconder Overlay de Bases
function hideBaseOverlay() {
  const overlay = document.getElementById('base-overlay');

  overlay.classList.remove('opacity-100');
  overlay.classList.add('opacity-0');

  setTimeout(() => {
    overlay.classList.add('hidden');
  }, 500);
}
//Enseña los input para ingresar las coordenadas de la base que se va a crear
function showCoordsInput(){
    const coordsInput = document.getElementById('coordsInput');
    const coordsButton = document.getElementById('coordsButton');

    coordsInput.classList.remove('hidden');
    coordsButton.classList.remove('hidden');

    setTimeout(() => {
        coordsInput.classList.remove('opacity-0');
        coordsButton.classList.remove('opacity-0');
        coordsInput.classList.add('opacity-100');
        coordsButton.classList.add('opacity-100');
    }, 10);
}
//Oculta los input de las coordenadas de la base que se creó
function hideCoordsInput(){
    const coordsInput = document.getElementById('coordsInput');
    const coordsButton = document.getElementById('coordsButton');

    coordsInput.classList.remove('opacity-100');
    coordsButton.classList.remove('opacity-100');
    coordsInput.classList.add('opacity-0');
    coordsButton.classList.add('opacity-0');

    setTimeout(() => {
        coordsInput.classList.add('hidden');
        coordsButton.classList.add('hidden');
    }, 500);
}
//Agregar una base a la lista.
function addBase(){
    const lat = document.getElementById('lat-coord').value;
    const lon = document.getElementById('lon-coord').value;
    fetch('/api/base', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify({
            'vehicles': [],
            'coords': {
                'x': lat,
                'y': lon
            },
            'breakdownState': false
        })
    })
    .then(response => {
        if(!response.ok){
            throw new Error('Network Response was not OK when Adding Base');
        }

        return response.json();
    })
    .then(data => {
        console.log('success: ', data);
        setTimeout(() => {
            showLastBase();
            hideCoordsInput();
        }, 100);
    })
}
//AGREGAR UN SCOOTER A UNA BASE
async function addScooter(){
    const basesResponse = await fetch('/api/bases');
    const bases = await basesResponse.json();

    for (let index = 0; index < bases.length; index++) {
        const base = bases[index];
        if(base.id === selectedBase){
            console.log(base.id)
            fetch('/api/set-scooter', {
                method: 'POST',
                headers: {
                    'Content-Type':'application/json'
                },
                body: JSON.stringify({
                    'id': base.id,
                    'vehicles': base.vehicles,
                    'coords': {
                        'x': base.coords.x,
                        'y': base.coords.y
                    },
                    'breakdownState': base.breakdownState
                })
            })
            .then(response => {
                if(!response.ok){
                    throw new Error('Network Response was not OK when Adding Scooter');
                }

                return response.json();
            })
            .then(data => {
                console.log('success: ', data)
                setTimeout(() => {
                    location.reload();
                }, 50);
            })
        }
        
    }
}
//AGREGAR UNA BICICLETA A UNA BASE
async function addBicycle(){
    const basesResponse = await fetch('/api/bases');
    const bases = await basesResponse.json();

    for (let index = 0; index < bases.length; index++) {
        const base = bases[index];
        if(base.id === selectedBase){
            console.log(base.id)
            fetch('/api/set-bicycle', {
                method: 'POST',
                headers: {
                    'Content-Type':'application/json'
                },
                body: JSON.stringify({
                    'id': base.id,
                    'vehicles': base.vehicles,
                    'coords': {
                        'x': base.coords.x,
                        'y': base.coords.y
                    },
                    'breakdownState': base.breakdownState
                })
            })
            .then(response => {
                if(!response.ok){
                    throw new Error('Network Response was not OK when Adding Bicycle');
                }

                return response.json();
            })
            .then(data => {
                console.log('success: ', data)
                setTimeout(() => {
                    location.reload();
                }, 50);
            })
        }
        
    }
}
//AÑADIR FONDOS PARA CLIENTES
let open = false;
function addFunds() {
    const funds = document.getElementById("addFunds");

    if (!open) {
        funds.classList.remove('hidden');

        setTimeout(() => {
            funds.classList.remove('opacity-0');
            funds.classList.add('opacity-100');
        }, 10);
        open = true;
    } else {
        funds.classList.remove('opacity-100');
        funds.classList.add('opacity-0');

        setTimeout(() => {
            funds.classList.add('hidden');
        }, 500);
        open = false;
    }
}

function addBalance(funds){
    fetch('/api/add-balance', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify({
            'balance': funds
        })
    })
    .then(response => {
        if(!response.ok){
            throw new Error('Network Response was not OK');
        }
        return response.json();
    })
    .then(data => {
        console.log('success: ', data);
    })
    .then(() => {
        fetch('/api/add-balance')
            .then(response => response.json())
            .then(data => {
                document.getElementById('balance').textContent = data.toFixed(2);
                console.log(data);
            });
    })
    .catch(error => {
        console.log('error: ', error);
    })
}

//Obtener geolocalización del usuario
function initGeoLocation(){
    if (navigator.geolocation) {
    navigator.geolocation.getCurrentPosition(
        function (position) {
            const lat = position.coords.latitude;
            const lon = position.coords.longitude;

            // Centrar el mapa
            map.setView([lat, lon], 15);

            // Agregar pin
            userMarker = L.marker([lat, lon], {icon: myIcon}).addTo(map);
            userMarker.bindPopup("📍 Tu ubicación actual").openPopup();
        },
        function (error) {
            alert("Error obteniendo la ubicación: " + error.message);
        }
    );
    } else {
        alert("Geolocalización no es soportada en este navegador.");
    }
}