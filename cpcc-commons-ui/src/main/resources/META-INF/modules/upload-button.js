define([ "jquery", "t5/core/console", "bootstrap/modal" ],

function($, console)
{
	return function(id, showModal)
	{
		$(document).on('change', '.btn-file :file', function()
		{
			var input = $(this);
			var numFiles = input.get(0).files ? input.get(0).files.length : 1;
			var label = input.val().replace(/\\/g, '/').replace(/.*\//, '');
			input.trigger('fileselect', [ numFiles, label ]);
		});

		function onFileSelect(event, numFiles, label)
		{
			var input = $(this).parents('.input-group').find(':text');
			var log = numFiles > 1 ? numFiles + ' files selected' : label;

			if (input.length)
			{
				input.val(log);
			}
			else
			{
				if (log)
				{
					alert(log);
				}
			}
		}

		$(document).ready(function()
		{
			$('.btn-file :file').on('fileselect', onFileSelect);
		});

		console.info("showModal " + id + " is " + showModal);
		if (showModal)
		{
			$('#' + id).modal({
				show : true
			})
		}
	}
});