import {csrfHeader, csrfToken} from "./util/csrf.js";

const context = document.body.dataset.context;
const customerId = document.body.dataset.customerId;

async function loadBikes() {
    const tbody = document.getElementById("bikeTableBody");
    tbody.innerHTML = "<tr><td colspan='5'>Loading...</td></tr>";

    try {
        let url = "";

        if (context === "customer-ebikes") {
            url = `/api/ebikes/customers/${customerId}`;
        } else if (context === "all-ebikes") {
            url = `/api/ebikes`;
        } else {
            throw new Error("Unknown context: " + context);
        }

        const response = await fetch(url);
        if (!response.ok) throw new Error("Failed to fetch bikes");

        const bikes = await response.json();
        if (bikes.length === 0) {
            tbody.innerHTML = '<tr><td colspan="6">No bikes found.</td></tr>';
        } else {
            tbody.innerHTML = bikes.map(bike => `
                <tr>
                  <td>${bike.id}</td>
                  <td>${bike.brand}</td>
                  <td>${bike.model}</td>
                  <td>${bike.battery}</td>
                  <td>${bike.ebikeModelDto?.name || 'N/A'}</td>

                  ${context === "all-ebikes" ? `<td>${bike.customerCount}</td>` : ""}
                  <td>
                    ${context === "customer-ebikes" ? `
                        <button class="btn btn-sm btn-outline-primary run-test-btn" data-id="${bike.id}">Run Test</button>
                    ` : ""}
                    <button class="btn btn-sm btn-danger delete-bike-btn ms-2" data-id="${bike.id}">
                        ${context === "customer-ebikes" ? "Remove Link" : "Delete eBike"}
                    </button>
                    <button class="btn btn-sm btn-info bike-test-btn ms-2" data-id="${bike.id}">Tests
                    </button>
                  </td>
                </tr>
            `).join("");
        }

        if (context === "customer-ebikes") {
            attachRunTestHandlers();
        }
        attachTestRedirector();
        attachDeleteHandlers();

    } catch (err) {
        console.error("Error loading bikes:", err);
        tbody.innerHTML = '<tr><td colspan="6">Error loading bikes.</td></tr>';
    }
}

function attachTestRedirector() {
    document.querySelectorAll(".bike-test-btn").forEach(btn => {
        btn.addEventListener("click", async () => {
            const bikeId = btn.dataset.id;

            window.location.href = `/technician/bikes/${bikeId}/tests`;

        });
    });
}

function attachAddBikeHandler() {
    const form = document.getElementById("addBikeForm");
    if (!form) return;

    form.addEventListener("submit", async e => {
        e.preventDefault();
        const formData = new FormData(form);
        const dto = Object.fromEntries(formData.entries());

        try {
            const effectiveCustomerId = context === "customer-ebikes"
                ? customerId
                : form.querySelector("select[name='customerId']").value;

            const response = await fetch(`/api/customers/${effectiveCustomerId}/bikes`, {
                method: "POST",
                headers: {
                    "Content-Type": "application/json",
                    ...(csrfHeader && csrfToken ? {[csrfHeader]: csrfToken} : {})
                },
                body: JSON.stringify(dto)
            });
            if (!response.ok) {
                const error = await response.text();
                throw new Error(error);
            }
            const modal = document.getElementById("addBikeModal");
            const modalInstance = bootstrap.Modal.getOrCreateInstance(modal);
            modalInstance.hide();
            loadBikes();
        } catch (err) {
            console.error("Add bike failed:", err);
            alert("Failed to add bike: " + err.message);
        }
    });
}

function attachRunTestHandlers() {
    const hidden = document.getElementById("bikeIdInput");

    document.querySelectorAll(".run-test-btn").forEach(btn => {
        btn.addEventListener("click", async () => {
            const bikeId = btn.dataset.id;
            hidden.value = bikeId;

            try {
                const resp = await fetch(`/api/ebikes/${bikeId}`);
                if (resp.ok) {
                    const bikeData = await resp.json();
                    const model = bikeData.ebikeModelDto || {};

                    const setValue = (id, value) => {
                        const el = document.getElementById(id);
                        if (el) el.value = value ?? "";
                    };

                    setValue("batteryCapacity", model.batteryCapacity);
                    setValue("maxSupport", model.maxSupport);
                    setValue("enginePowerMax", model.enginePowerMax);
                    setValue("enginePowerNominal", model.enginePowerNominal);
                    setValue("engineTorque", model.engineTorque);
                }
            } catch (e) {
                console.warn("Could not prefill test form:", e);
                ["batteryCapacity", "maxSupport", "enginePowerMax", "enginePowerNominal", "engineTorque"]
                    .forEach(id => document.getElementById(id).value = "");
            }

            const modal = document.getElementById("runTestModal");
            const modalInstance = bootstrap.Modal.getOrCreateInstance(modal);
            modalInstance.show();
        });
    });
}

function attachRunTestFormHandler() {
    const form = document.getElementById("runTestForm");

    if (form.dataset.listenerAttached === "true") return; // prevent duplicate binding
    form.dataset.listenerAttached = "true";

    form.addEventListener("submit", async e => {
        e.preventDefault();

        const dto = {
            type: document.getElementById("testType").value,
            batteryCapacity: document.getElementById("batteryCapacity").value,
            maxSupport: document.getElementById("maxSupport").value,
            enginePowerMax: document.getElementById("enginePowerMax").value,
            enginePowerNominal: document.getElementById("enginePowerNominal").value,
            engineTorque: document.getElementById("engineTorque").value,
            ebikeId: parseInt(document.getElementById("bikeIdInput").value),
            customerId: parseInt(customerId),
            technicianId: null,
            uuid: ""
        };

        try {
            const response = await fetch("/api/test", {
                method: "POST",
                headers: {
                    "Content-Type": "application/json",
                    ...(csrfHeader && csrfToken ? {[csrfHeader]: csrfToken} : {})
                },
                body: JSON.stringify(dto)
            });

            if (!response.ok) {
                const error = await response.text();
                throw new Error(error);
            }

            const modalEl = document.getElementById('runTestModal');
            const modalInstance = bootstrap.Modal.getOrCreateInstance(modalEl);
            modalInstance.hide();
            alert(`Test started!`);
            window.location.href = '/technician/tests';
        } catch (err) {
            console.error("Test failed:", err);
            alert("Failed to start test: " + err.message);
        }
    });
}

function attachDeleteHandlers() {
    document.querySelectorAll(".delete-bike-btn").forEach(btn => {
        btn.addEventListener("click", async () => {
            const bikeId = btn.dataset.id;

            let url, confirmMessage;

            if (context === "customer-ebikes") {
                url = `/api/ebikes/customers/${customerId}/${bikeId}`;
                confirmMessage = "Remove this bike from the customer?";
            } else if (context === "all-ebikes") {
                url = `/api/ebikes/${bikeId}`;
                confirmMessage = "This will permanently delete the eBike and all its data. Continue?";
            } else {
                console.error("Unknown context for delete operation");
                return;
            }

            if (!confirm(confirmMessage)) return;

            try {
                const response = await fetch(url, {
                    method: "DELETE",
                    headers: {
                        ...(csrfHeader && csrfToken ? {[csrfHeader]: csrfToken} : {})
                    }
                });

                if (response.ok) {
                    loadBikes();
                } else if (response.status === 409) {
                    alert('Cannot delete this bike! Bike is in use.');
                } else {
                    const errorText = await response.text();
                    alert('Failed to delete model: ' + errorText);
                }
            } catch (err) {
                console.error("Delete failed:", err);
                alert("Failed to delete: " + err.message);
            }
        });
    });
}

document.addEventListener("DOMContentLoaded", () => {
    loadBikes();
    attachAddBikeHandler();
    attachRunTestFormHandler(); //
});