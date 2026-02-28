document.addEventListener("DOMContentLoaded", function () {
    const torqueCtx = document.getElementById('torqueGraph').getContext('2d');
    const batteryCtx = document.getElementById('batteryGraph').getContext('2d');

    new Chart(torqueCtx, {
        type: 'line',
        data: {
            labels: ['0 km', '100 km', '200 km', '300 km', '400 km'],
            datasets: [{
                label: 'Torque (Nm)',
                data: [85, 80, 78, 75, 72],
                borderColor: 'white',
                backgroundColor: 'rgba(255, 255, 255, 0.1)',
                borderWidth: 2
            }]
        },
        options: {
            responsive: true,
            scales: {
                y: { beginAtZero: false, ticks: { color: 'white' } },
                x: { ticks: { color: 'white' } }
            }
        }
    });

    new Chart(batteryCtx, {
        type: 'line',
        data: {
            labels: ['0 km', '100 km', '200 km', '300 km', '400 km'],
            datasets: [{
                label: 'Battery Life (%)',
                data: [100, 90, 78, 65, 50],
                borderColor: 'white',
                backgroundColor: 'rgba(255, 255, 255, 0.1)',
                borderWidth: 2
            }]
        },
        options: {
            responsive: true,
            scales: {
                y: { beginAtZero: true, ticks: { color: 'white' } },
                x: { ticks: { color: 'white' } }
            }
        }
    });
});
