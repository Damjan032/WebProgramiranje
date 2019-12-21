$("#header").load("header.html");
$("#footer").load("footer.html");
$("form").addEventListener("submit",function(event){
    let inputs =  document.getElementsByTagName("input");
    let flag = false;
    for(let input of inputs){
        if(input.type=="text" || input.type=="number"){
            if(!input.value){
                input.style.borderColor = "red";
                flag = true;
            }
        }
        if(input.get)
    }
    if(flag){
        event.preventDefault();
    }



