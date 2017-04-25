'use strict';

/**
 * Ugly java script code for simple tests of shareit's REST interface.
 *  @author Axel Böttcher <axel.boettcher@hm.edu>
 */

/**
 * This function is used for transfer of new book info.
 */
var submitNewBook = function() {
	var json = JSON.stringify({
			title: $("input[name=title]").val(),
			author: $("input[name=author]").val(),
			isbn: $("input[name=isbn]").val()
	});
	var errorText = $("#errormessageBook");
    $.ajax({
        url: '/shareit/media/books/',
        type:'POST',
        contentType: 'application/json; charset=UTF-8',
        data: json
        })
        .done(() => {
			$("input[name=title]").val("");
			$("input[name=author]").val("");
			$("input[name=isbn]").val("");
        	
        	errorText.removeClass("visible");
        	errorText.addClass("hidden");
        })
        .fail((error) => {
        	errorText.addClass("visible");
        	errorText.text(error.responseJSON.detail);
        	errorText.removeClass("hidden");
        });
}


/**
 * This function is used for transfer of new disc info.
 */
var submitNewDisc = function() {
    var json = JSON.stringify({
        title: $("input[name=title]").val(),
        director: $("input[name=director]").val(),
        barcode: $("input[name=barcode]").val(),
        fsk: $("input[name=fsk]").val()
    });
    var errorText = $("#errormessageBook");
    $.ajax({
        url: '/shareit/media/discs/',
        type:'POST',
        contentType: 'application/json; charset=UTF-8',
        data: json
    })
        .done(() => {
        $("input[name=title]").val("");
    $("input[name=director]").val("");
    $("input[name=barcode]").val("");
    $("input[name=fsk]").val("");

    errorText.removeClass("visible");
    errorText.addClass("hidden");
})
    .fail((error) => {
        errorText.addClass("visible");
    errorText.text(error.responseJSON.detail);
    errorText.removeClass("hidden");
});
}
/**
 * Creates a list of all books using a Mustache-template.
 */
var listBooks = function() {
	$.ajax({
        url: '/shareit/media/books',
        type:'GET'
	})
	.done((data) => {
		var template = "<h2>ShareIt - List of all Books</h2><table class='u-full-width'><tbody>{{#data}}<tr><td>{{title}}</td><td>{{author}}</td><td>{{isbn}}</td></tr>{{/data}}</tbody></table>";
		Mustache.parse(template);
		var output = Mustache.render(template, {data: data});
		$("#content").html(output);
	});// no error handling
}

/**
 * Creates a list of all discs using a Mustache-template.
 */
var listDiscs = function() {
    $.ajax({
        url: '/shareit/media/discs',
        type:'GET'
    })
        .done((data) => {
        var template = "<h2>ShareIt - List of all Discs</h2><table class='u-full-width'><tbody>{{#data}}<tr><td>{{title}}</td><td>{{director}}</td><td>{{barcode}}</td><td>{{fsk}}</td></tr>{{/data}}</tbody></table>";
    Mustache.parse(template);
    var output = Mustache.render(template, {data: data});
    $("#content").html(output);
});// no error handling
}

/**
 * Call backer for "navigational buttons" in left column. Used to set content in main part.
 */
var changeContentBook = function(content) {
	$.ajax({
        url: content,
        type:'GET'
	})
	.done((data) => {
		$("#content").html(data);
	});// no error handling
}
var changeContentDisc = function(content) {
    $.ajax({
        url: content,
        type:'GET'
    })
        .done((data) => {
        $("#content").html(data);
});// no error handling
}