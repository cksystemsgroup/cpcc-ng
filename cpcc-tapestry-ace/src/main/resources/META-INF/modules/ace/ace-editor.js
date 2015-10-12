define([ "jquery", "ace", "t5/core/console" ],

function($, ace, console)
{
	var module = {};

	module.editors = [];

	module.init = function(id, options)
	{
		var textarea = $('#' + id);

		var editDiv = $('<div>', {
			position : 'absolute',
			width : '100%',
			height : textarea.height(),
			'class' : textarea.attr('class')
		}).insertBefore(textarea);

		textarea.css('visibility', 'hidden');
		textarea.css('width', '0');
		textarea.css('height', '0');

		var editor = ace.edit(editDiv[0]);
		editor.getSession().setValue(textarea.val());

		editor.setAnimatedScroll(options.animatedScroll);
		editor.renderer.setHScrollBarAlwaysVisible(options.hScrollBarAlwaysVisible);
		editor.setPrintMarginColumn(options.printMarginColumn);
		editor.renderer.setPadding(options.padding);
		editor.setReadOnly(options.readOnly);
		editor.renderer.setShowGutter(options.showGutter);
		editor.setShowInvisibles(options.showInvisibles);
		editor.setShowPrintMargin(options.showPrintMargin);

		editor.setTheme("ace/theme/" + options.theme);
		editor.getSession().setMode("ace/mode/" + options.mode);

		editor.getSession().on('change', function()
		{
			textarea.val(editor.getSession().getValue());
		});

		module.editors[id] = editor;
	};

	return module;
});