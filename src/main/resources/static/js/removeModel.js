function removeModel(modelId) {

    $.ajax({
        type: "POST",
        url: "/api/remove-model",
        data: { modelId: modelId },
        success: function(response) {
            $('#model_' + modelId).fadeOut(500, function() {
                            $(this).remove();
                        });
        },
        error: function(error) {
            alert("Error removing model");
        }
    });

    return false;
}
