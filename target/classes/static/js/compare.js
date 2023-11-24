populateDropdowns();

function resetDropdown(dropdown) {
    dropdown.innerHTML = '<option value="" selected>Select...</option>';
}

function clearAllDropdowns() {
    resetDropdown(document.getElementById('brand1'));
    resetDropdown(document.getElementById('model1'));
    resetDropdown(document.getElementById('details1'));

    resetDropdown(document.getElementById('brand2'));
    resetDropdown(document.getElementById('model2'));
    resetDropdown(document.getElementById('details2'));
}

async function populateDropdowns() {
    clearAllDropdowns();
    const brandsResponse = await fetch('/api/brands');
    const brands = await brandsResponse.json();

    const brand1Select = document.getElementById('brand1');
    const model1Select = document.getElementById('model1');
    const details1Select = document.getElementById('details1');

    const brand2Select = document.getElementById('brand2');
    const model2Select = document.getElementById('model2');
    const details2Select = document.getElementById('details2');

    brands.forEach(brand => {
        const option = document.createElement('option');
        option.value = brand.id;
        option.text = brand.name;
        brand1Select.appendChild(option);

        const option2 = document.createElement('option');
        option2.value = brand.id;
        option2.text = brand.name;
        brand2Select.appendChild(option2);
    });

    brand1Select.addEventListener('change', async () => {
        const selectedBrandId = brand1Select.value;

        const modelsResponse = await fetch(`/api/brands/${selectedBrandId}/models`);
        const models = await modelsResponse.json();

        resetDropdown(document.getElementById('model1'));

        models.forEach(model => {
            const option = document.createElement('option');
            option.value = model.id;
            option.text = model.name;
            model1Select.appendChild(option);
        });

        model1Select.addEventListener('change', async () => {
            const selectedModelId = model1Select.value;

            const detailsResponse = await fetch(`/api/models/${selectedModelId}/details`);
            const details = await detailsResponse.json();

            resetDropdown(document.getElementById('details1'));

            details.forEach(detail => {
                const option = document.createElement('option');
                option.value = detail.id;
                option.text = detail.engine.size;
                details1Select.appendChild(option);
            });

            details1Select.addEventListener('change', () => {
                checkCompareButtonState();
            });

        });
    });

    brand2Select.addEventListener('change', async () => {
        const selectedBrandId = brand2Select.value;

        const modelsResponse = await fetch(`/api/brands/${selectedBrandId}/models`);
        const models = await modelsResponse.json();

        resetDropdown(document.getElementById('model2'));

        models.forEach(model => {
            const option = document.createElement('option');
            option.value = model.id;
            option.text = model.name;
            model2Select.appendChild(option);
        });

        model2Select.addEventListener('change', async () => {
            const selectedModelId = model2Select.value;

            const detailsResponse = await fetch(`/api/models/${selectedModelId}/details`);
            const details = await detailsResponse.json();

            resetDropdown(document.getElementById('details2'));

            details.forEach(detail => {
                const option = document.createElement('option');
                option.value = detail.id;
                option.text = detail.engine.size;
                details2Select.appendChild(option);
            });
            details2Select.addEventListener('change', () => {
                checkCompareButtonState();
            });
        });
    });

    brand1Select.dispatchEvent(new Event('change'));
    brand2Select.dispatchEvent(new Event('change'));
}

function checkCompareButtonState() {
    console.log("Checking Compare Button State");
    const details1Select = document.getElementById('details1');
    const details2Select = document.getElementById('details2');
    const compareButton = document.getElementById('compareButton');

    const details1Selected = details1Select.value !== '';
    const details2Selected = details2Select.value !== '';
    const detailsNotEqual = details1Select.value !== details2Select.value;

    compareButton.style.display = (details1Selected && details2Selected && detailsNotEqual) ? 'inline-block' : 'none';
}

function compareCars() {
    const details1Select = document.getElementById('details1');
    const details2Select = document.getElementById('details2');

    const detail1Id = details1Select.value;
    const detail2Id = details2Select.value;

    const compareUrl = `/compare/${detail1Id}/compare-to/${detail2Id}`;
    window.location.href = compareUrl;
}
