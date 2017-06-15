/*
 * jTimepicker plugin 1.4.1
 *
 * http://www.radoslavdimov.com/jquery-plugins/jquery-plugin-jtimepicker/
 *
 * Copyright (c) 2009 Radoslav Dimov
 *
 * Dual licensed under the MIT and GPL licenses:
 * http://www.opensource.org/licenses/mit-license.php
 * http://www.gnu.org/licenses/gpl.html
 *
 * Depends:
 *      ui.core.js
 *      ui.slider.js
 */

(function($) {
    $.fn.extend({
		
        jtimecodepicker: function(options) {
            
            var defaults = {
            	componentId:'comPonentId',
                clockIcon: 'images/icon_clock_2.gif',
                orientation: 'horizontal',
                // set hours
                hourCombo: 'hourcombo',
                hourMode: 60,
                hourInterval: 1,
                hourDefaultValue: '00',
                hourSlider: 'hourSlider',
                hourLabel: '时',
                // set minutes
                minCombo: 'mincombo',
                minLength: 60,
                minInterval: 1,
                minDefaultValue: '00',
                minSlider: 'minSlider',
                minLabel: '分',
                // set seconds
                secView: true,
                secCombo: 'seccombo',
                secLength: 60,
                secInterval: 1,
                secDefaultValue: '00',
                secSlider: 'secSlider',
                secLabel: '秒',
				// set frames
				frameView: true,
                frameCombo: 'framecombo',
                frameLength: 25,
                frameInterval: 1,
                frameDefaultValue: '00',
                frameSlider: 'frameSlider',
                frameLabel: '帧',
                sliderWrapId:'sliderWrap'
            };

            var options = $.extend(defaults, options);
            
            return this.each(function() {
                var o = options;
                var $this = $(this);
                var html = '';
                var orientation = (o.orientation == 'horizontal') ? 'auto' : 'vertical';
                
                var hourId = o.componentId + "Hour";
                var minId = o.componentId + "Min";
                var secId = o.componentId + "Sec";
                var frameId = o.componentId + "Frame";
                
                var sliderHour = o.componentId +"Slider"+ "Hour";
                var sliderMin = o.componentId +"Slider"+ "Min";
                var sliderSec = o.componentId +"Slider"+ "Sec";
                var sliderFrame = o.componentId +"Slider"+ "Frame";
                
                var sliderData = [
                                    {'label':o.hourLabel, 'slider':sliderHour, 'combo':hourId},
                                    {'label':o.minLabel, 'slider':sliderMin, 'combo':minId}
                                    ];
                
                html += $this.createCombo(hourId, o.hourMode, o.hourInterval, o.hourDefaultValue);
                html += $this.createCombo(minId, o.minLength, o.minInterval, o.minDefaultValue);
                if (o.secView) {
                    sliderData.push({'label':o.secLabel, 'slider':sliderSec, 'combo':secId});
                    html += $this.createCombo(secId, o.secLength, o.secInterval, o.secDefaultValue);
                }

				sliderData.push({'label':o.frameLabel, 'slider':sliderFrame, 'combo':frameId});
				html += $this.createCombo(frameId, o.frameLength, o.frameInterval, o.frameDefaultValue);

                //html += '<img src="' + o.clockIcon + '" class="clock" />';
                //html += $this.createSliderWrap(sliderData,o.componentId+"Slider");
                $this.html(html);
                
                $('.sliderWrap').addClass(orientation);
                
                $this.createSlider(sliderHour, o.hourMode, hourId, o.hourInterval, o.hourDefaultValue, o.orientation);
                $this.createSlider(sliderMin, o.minLength, minId, o.minInterval, o.minDefaultValue, o.orientation);
                if (o.secView) {
                    $this.createSlider(sliderSec, o.secLength, secId, o.secInterval, o.secDefaultValue, o.orientation);
                }

				$this.createSlider(sliderFrame, o.frameLength, frameId, o.frameInterval, o.frameDefaultValue, o.orientation);

                $.each(sliderData, function(i, item) {
                    $('.' + item.combo).change(function() {
                        var val = $(this).val();
                        $('.' + item.slider).slider('option', 'value', val);
                    });
                });                

                $this.find('.clock').click(function() {
                    $this.find('.sliderWrap').toggle(function() {
                        $(document).click(function(event) {
                            if (!($(event.target).is('.sliderWrap') || $(event.target).parents('.sliderWrap').length || $(event.target).is('.clock'))) {
                                $this.find('.sliderWrap').hide(500);
                            }
                        });
                    });  
                });              
            }); 
        }     
    });

    $.fn.createCombo = function(id, length, interval, defValue) {

		//var html = '<input type=text readonly="true" style="width:30px;" class="' + id + ' combo" name="' + id + '" value="'+defValue+'" />';

        var html = '<select class="' + id + ' combo" name="' + id + '">';
        for(i = 0; i < length; i += interval) {
            var selected = i == defValue ? ' selected="selected"' : '';
            var txt = i < 10 ? '0' + i : i;
            html += '<option value="' + txt + '"' + selected + '>' + txt + '</option>';
        }
        html += "</select>";
        return html;
    }

    $.fn.createSliderWrap = function(data,sliderWrapId) {
        var html = '<div id="'+sliderWrapId+'" class="sliderWrap">';
        $.each(data, function(i, item) {
            html += '   <div><label>' + item.label + ':</label> <p class="' + item.slider + '"></p></div>';
        });
            html += '</div>';

        return html;
    }

    $.fn.createSlider = function(id, maxValue, combo, stepValue, defValue, orientation) {
    	//alert(id);
        var $this = $(this);
        $this.find('.' + id).slider({
            orientation: orientation,
            range: "min",
            min: 0,
            max: maxValue - stepValue,
            value: defValue,
            step: stepValue,
            animate: true ,
            slide: function(event, ui) {
        		var $uiValue = ui.value;
        		var txt = $uiValue < 10 ? '0' + $uiValue : $uiValue;
                $this.find('.' + combo).val(txt);
            }            
        });
    }
    
    $.fn.getTimeCodeValue = function (){
    	var $this = $(this);
    	var id = $this.attr('id');
    	var $obj = $('#'+id+' select');
		var $size = $obj.length;
		var $value = '';
		for (var i=0; i<$size ; i++){
			if ( i == 0) {
				$value = $value + $($obj[i]).val();
			} else {
				$value =  $value + ':' + $($obj[i]).val();
			}						
		}
		return $value;
    }
})(jQuery);
