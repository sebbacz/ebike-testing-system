import { csrfHeader, csrfToken } from "./util/csrf.js";

let editModelId = null; // Track if editing (still keep for JS convenience)

// Load models into the table
async function loadModels() {
    const tbody = document.getElementById('model-table-body');
    try {
        const response = await fetch('/api/ebikes/models');
        const models = await response.json();

        tbody.innerHTML = models.map(model => `
            <tr>
                <td>${model.id}</td>
                <td>${model.name}</td>
                <td>${model.batteryCapacity}</td>
                <td>${model.maxSupport}</td>
                <td>${model.enginePowerMax}</td>
                <td>${model.enginePowerNominal}</td>
                <td>${model.engineTorque}</td>
                <td>${model.ebikeCount}</td>
                <td>
                    <button class="btn btn-sm btn-primary edit-model-btn" data-id="${model.id}">
                        Edit
                    </button>
                    <button class="btn btn-sm btn-danger delete-model-btn ms-2" data-id="${model.id}">
                        Delete
                    </button>
                </td>
            </tr>
        `).join('');
    } catch (err) {
        console.error('Failed to load models:', err);
        tbody.innerHTML = '<tr><td colspan="9">Error loading models</td></tr>';
    }

    attachEditHandlers();
    attachDeleteHandlers();
}

function attachEditHandlers() {
    document.querySelectorAll('.edit-model-btn').forEach(btn => {
        btn.addEventListener('click', async () => {
            const id = btn.getAttribute('data-id');
            try {
                const res = await fetch(`/api/ebikes/models/${id}`);
                if (!res.ok) throw new Error('Model not found');
                const model = await res.json();

                // Fill form fields
                document.getElementById('name').value = model.name;
                document.getElementById('batteryCapacity').value = model.batteryCapacity;
                document.getElementById('maxSupport').value = model.maxSupport;
                document.getElementById('enginePowerMax').value = model.enginePowerMax;
                document.getElementById('enginePowerNominal').value = model.enginePowerNominal;
                document.getElementById('engineTorque').value = model.engineTorque;

                // Set hidden input and variable
                document.getElementById('editModelId').value = model.id;
                editModelId = model.id;

                // Change modal title and button text
                document.getElementById('addBikeModelLabel').textContent = 'Edit eBike Model';
                document.getElementById('model-submit-btn').textContent = 'Save';

                // Show modal
                const modal = new bootstrap.Modal(document.getElementById('addBikeModel'));
                modal.show();

            } catch (err) {
                console.error(err);
                alert('Could not load model for editing.');
            }
        });
    });
}

function attachDeleteHandlers() {
    document.querySelectorAll('.delete-model-btn').forEach(btn => {
        btn.addEventListener('click', async () => {
            const id = btn.getAttribute('data-id');
            const confirmed = confirm('Are you sure you want to delete this model?');
            if (!confirmed) return;

            try {
                const response = await fetch(`/api/ebikes/models/${id}`, {
                    method: 'DELETE',
                    headers: {
                        ...(csrfHeader && csrfToken ? { [csrfHeader]: csrfToken } : {})
                    }
                });

                if (response.ok) {
                    loadModels();
                } else if (response.status === 409) {
                    alert('Cannot delete this model because it is attached to one or more eBikes.');
                } else {
                    const errorText = await response.text();
                    alert('Failed to delete model: ' + errorText);
                }
            } catch (err) {
                console.error('Error deleting model:', err);
                alert('An error occurred while deleting the model.');
            }
        });
    });
}

function isValidNumber(value) {
    return !isNaN(value) && Number(value) > 0;
}

document.addEventListener('DOMContentLoaded', () => {
    loadModels();

    const form = document.getElementById('add-ebike-model-form');

    form.addEventListener('submit', async (e) => {
        e.preventDefault();

        // Read values
        const name = document.getElementById('name').value.trim();
        const batteryCapacity = document.getElementById('batteryCapacity').value;
        const maxSupport = document.getElementById('maxSupport').value;
        const enginePowerMax = document.getElementById('enginePowerMax').value;
        const enginePowerNominal = document.getElementById('enginePowerNominal').value;
        const engineTorque = document.getElementById('engineTorque').value;

        // Read hidden input
        const hiddenIdValue = document.getElementById('editModelId').value;
        const id = hiddenIdValue && Number(hiddenIdValue) > 0 ? Number(hiddenIdValue) : null;

        if (!name || !isValidNumber(batteryCapacity) || !isValidNumber(maxSupport) ||
            !isValidNumber(enginePowerMax) || !isValidNumber(enginePowerNominal) ||
            !isValidNumber(engineTorque)) {
            alert('Please fill in all fields correctly.');
            return;
        }

        const modelData = {
            name,
            batteryCapacity: Number(batteryCapacity),
            maxSupport: Number(maxSupport),
            enginePowerMax: Number(enginePowerMax),
            enginePowerNominal: Number(enginePowerNominal),
            engineTorque: Number(engineTorque)
        };

        const isEditing = typeof editModelId === 'number' && editModelId > 0;
        const method = isEditing ? 'PUT' : 'POST';
        const url = isEditing ? `/api/ebikes/models/${editModelId}` : '/api/ebikes/models';

        try {
            const response = await fetch(url, {
                method: method,
                headers: {
                    'Content-Type': 'application/json',
                    'Accept': 'application/json',
                    ...(csrfHeader && csrfToken ? { [csrfHeader]: csrfToken } : {})
                },
                body: JSON.stringify(modelData)
            });

            if (response.ok) {
                const result = await response.json();
                alert(`Model '${result.name}' ${id ? 'updated' : 'added'} successfully!`);
                form.reset();

                // Reset hidden input and JS var
                document.getElementById('editModelId').value = '';
                editModelId = null;

                // Reset modal UI
                document.getElementById('addBikeModelLabel').textContent = 'Add New eBike Model';
                document.getElementById('model-submit-btn').textContent = 'Add Model';

                // Hide modal
                const modal = bootstrap.Modal.getInstance(document.getElementById('addBikeModel'));
                modal.hide();

                // Reload list
                loadModels();

            } else {
                const errorText = await response.text();
                alert(`Failed to ${id ? 'update' : 'add'} model: ` + errorText);
            }
        } catch (err) {
            console.error(`Error ${id ? 'updating' : 'adding'} model:`, err);
            alert('An error occurred while saving the model.');
        }
    });
});
