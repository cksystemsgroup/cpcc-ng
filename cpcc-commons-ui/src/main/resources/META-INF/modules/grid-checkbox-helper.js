define([ "jquery" ],

function($)
{
	return function(gridId, checkBoxId, enableIds, allCheckboxColumn, allCheckboxChecked)
	{
		var $grid = $('#' + gridId);
		var $buttons = enableIds.trim().split(",").map(function(e)
		{
			return $('#' + e);
		});

		var $checkBoxes = $grid.find('input[id^=' + checkBoxId + ']');

		$checkBoxes.each(function(e)
		{
			$(this).on('click', enableButtons)
		});

		var $allCB = null;

		if (allCheckboxColumn >= 0)
		{
			var allCbId = 'all_' + checkBoxId;

			var $thead = $grid.find('thead');
			var $tr = $thead.find('tr');
			var $th = $tr.find('th');
			var $thx = $th[allCheckboxColumn];

			var html = '<input ';
			if (allCheckboxChecked)
			{
				html += 'checked="checked"';
			}
			html += ' id="' + allCbId + '" type="checkbox" />';
			$thx.innerHTML = html;

			function toggleAllCheckBoxes()
			{
				var checked = $allCB.prop('checked');

				$checkBoxes.each(function(e)
				{
					$(this).prop('checked', checked);
				});

				$buttons.forEach(function(e)
				{
					e.prop("disabled", !checked)
				});
			}

			$allCB = $thead.find('#' + allCbId);
			$allCB.on('click', toggleAllCheckBoxes);
		}

		function enableButtons()
		{
			function addUp(previousValue, currentValue)
			{
				return previousValue + currentValue;
			}

			var sum = 0;
			$checkBoxes.each(function()
			{
				if ($(this).prop('checked'))
				{
					++sum;
				}
			});

			$buttons.forEach(function(e)
			{
				e.prop("disabled", sum === 0);
			});

			if ($allCB)
			{
				$allCB.prop('checked', false);
			}
		}
	};
});
