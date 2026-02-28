import { csrfHeader, csrfToken } from "./util/csrf.js";

document.addEventListener("DOMContentLoaded", () => {
    const customerSelect = document.getElementById("customerId");
    const ebikeSelect = document.getElementById("ebikeId");

    if (!customerSelect || !ebikeSelect) return;

    customerSelect.addEventListener("change", async () => {
        const custId = customerSelect.value;

        if (!custId) return;

        ebikeSelect.disabled = true;
        ebikeSelect.innerHTML = '<option value="">Loading...</option>';

        try {
            const response = await fetch(`/api/customers/${custId}/bikes`, {
                method: "GET",
                headers: {
                    "Content-Type": "application/json",
                    ...(csrfHeader && csrfToken ? { [csrfHeader]: csrfToken } : {})
                }
            });
            if (!response.ok) throw new Error("Failed to fetch eBikes");

            const ebikes = await response.json();

            ebikeSelect.innerHTML = '<option value="" disabled selected>Select eBike</option>';

            ebikes.forEach(bike => {
                const option = document.createElement("option");
                option.value = bike.id;
                option.textContent = `#${bike.id} - ${bike.model ?? 'Unnamed'}`;
                ebikeSelect.appendChild(option);
            });

            ebikeSelect.disabled = false;
        } catch (error) {
            console.error("Error loading eBikes:", error);
            ebikeSelect.innerHTML = '<option value="" disabled selected>No eBikes found</option>';
            ebikeSelect.disabled = true;
        }
    });
});
