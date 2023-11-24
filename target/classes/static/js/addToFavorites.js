function addToFavorites(modelId) {

    $.ajax({
        type: "POST",
        url: "/api/add-to-favorites",
        data: { modelId: modelId },
        success: function(response) {
            alert("Model added to favorites successfully");
        },
        error: function(error) {
            alert("Error adding to favorites");
        }
    });

    return false;
}
