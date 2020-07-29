package com.applifehack.knowledge.ui.activity.webview;

public class WebViewJavaScriptLoad {
    String htmlFilter = "html {-webkit-filter: invert(100%);' +    '-moz-filter: invert(100%);' +     '-o-filter: invert(100%);' +     '-ms-filter: invert(100%); }";

    public String getShowCodeElement() {
        return "javascript:window.touchblock=!window.touchblock;setTimeout(function(){$$.blocktoggle(window.touchblock)}, 100);";
    }

    public String getLoadHtml() {
        return "javascript:window.$$.processHTML('<html>'+document.getElementsByTagName('html')[0].innerHTML+'</html>');";
    }

    public String getModEditTextOn() {
        return "javascript:document.body.contentEditable = 'true'; document.designMode='on'; void 0";
    }

    public String getModEditTextOff() {
        return "javascript:document.body.contentEditable = 'false'; document.designMode='off'; void 0";
    }

    public String getLog() {
        return "javascript:function injek(){window.hasovrde=1;var e=XMLHttpRequest.prototype.open;XMLHttpRequest.prototype.open=function(ee,nn,aa){this.addEventListener('load',function(){$$.log(this.responseText, nn, JSON.stringify(arguments))}),e.apply(this,arguments)}};if(window.hasovrde!=1){injek();}";
    }

    public String getLog2() {
        return "javascript:function injek2(){window.touchblock=0,window.dummy1=1,document.addEventListener('click',function(n){if(1==window.touchblock){n.preventDefault();n.stopPropagation();var t=document.elementFromPoint(n.clientX,n.clientY);window.ganti=function(n){t.outerHTML=n},window.gantiparent=function(n){t.parentElement.outerHTML=n},$$.print(t.parentElement.outerHTML, t.outerHTML)}},!0)}1!=window.dummy1&&injek2();";
    }

    public String getLog3() {
        return "javascript:function injek3(){window.hasdir=1;window.dir=function(n){var r=[];for(var t in n)'function'==typeof n[t]&&r.push(t);return r}};if(window.hasdir!=1){injek3();}";
    }

    public String getDarkMode() {
        return "javascript:(d=>{var css=`:root{background-color:#fefefe;filter:invert(100%)}*{background-color:inherit}img:not([src*=\".svg\"]),video{filter: invert(100%)}`,style,id=\"dark-theme-snippet\",ee=d.getelementbyid(id);if(null!=ee)ee.parentnode.removechild(ee);else {style = d.createelement('style');style.type=\"text/css\";style.id=id;if(style.stylesheet)style.stylesheet.csstext=css;else style.appendchild(d.createtextnode(css));(d.head||d.queryselector('head')).appendchild(style)}})(document);";
    }

    public String getDarkModeInversia() {
        return "javascript: (function () { var css = 'html {-webkit-filter: invert(100%);' +    '-moz-filter: invert(100%);' +     '-o-filter: invert(100%);' +     '-ms-filter: invert(100%); }',head = document.getElementsByTagName('head')[0],style = document.createElement('style');if (!window.counter) { window.counter = 1;} else  { window.counter ++;if (window.counter % 2 == 0) { var css ='html {-webkit-filter: invert(0%); -moz-filter:    invert(0%); -o-filter: invert(0%); -ms-filter: invert(0%); }'}};style.type = 'text/css';if (style.styleSheet){style.styleSheet.cssText = css;} else {style.appendChild(document.createTextNode(css));}head.appendChild(style);}());";
    }
}