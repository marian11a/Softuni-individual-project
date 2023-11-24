function removeFromFavorites(modelId) {

    $.ajax({
        type: "POST",
        url: "/api/remove-from-favorites",
        data: { modelId: modelId },
        success: function(response) {
            $('#model_' + modelId).fadeOut(500, function() {
                $(this).remove();
            });
        },
        error: function(error) {
            alert("Error removing from favorites");
        }
    });

    return false;
}
