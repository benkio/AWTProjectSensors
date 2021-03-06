/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 * Log the response
 * @param {type} xmlhttp
 * @returns {undefined}
 */
function logResponse(xmlhttp){
    console.log(new XMLSerializer().serializeToString(xmlhttp.responseXML.documentElement));
}

/*
 * 
 * Pattern used to make the request and set a return function at response
 * 
 * @param {type} url to call
 * @param {type} loadFunction used to manage the response
 * @param {type} xmlRequest function to call for build the XML Request.
 * @returns {undefined}
 */
function XMLRequestPattern(url, loadFunction, xmlRequest, param) {
    var xmlhttp, xmlRequestDoc;
    $("#errorMessage").text("");
    if (param === undefined)
        xmlRequestDoc = xmlRequest();
    else 
        xmlRequestDoc = xmlRequest(param);
    
    if (window.XMLHttpRequest) { // code for IE7+, Firefox, Chrome, Opera, Safari
        xmlhttp = new XMLHttpRequest();
    } else { // code for IE6, IE5
        xmlhttp = new ActiveXObject("Microsoft.XMLHTTP");
    }
    xmlhttp.onreadystatechange = function () {
        if (xmlhttp.readyState === 4 && xmlhttp.status === 200) {
            if (param === undefined)
                loadFunction(xmlhttp);
            else
                loadFunction(xmlhttp,param);
        }
        else
            if (xmlhttp.status !== 200 && xmlhttp.status !== 0)
                $("#errorMessage").append("This Request Failed: "+ xmlRequestDoc.childNodes[0].nodeName); 
    };
    xmlhttp.open("POST", url, true);
    xmlhttp.setRequestHeader("Content-Type", "text/xml");
    xmlhttp.send(xmlRequestDoc);
}
