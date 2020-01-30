checkFormParams = function(){
    let email = $(".email");
    let emailMsg = $(".email-message");
    let index = 0;
    for(let e of email){
        if(!/^(([^<>()\[\]\\.,;:\s@"]+(\.[^<>()\[\]\\.,;:\s@"]+)*)|(".+"))@((\[[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}])|(([a-zA-Z\-0-9]+\.)+[a-zA-Z]{2,}))$/.test(e.value)){
            emailMsg[index].classList.remove("d-none");
            return false;
        }else{
            emailMsg[index].classList.add("d-none");
        }
    }
    let inputs = $(".required");
    let messages = $(".alert");
    // let inputs = document.getElementsByTagName("input");
    let flag = true;
    for(let i = 0;i<inputs.length;i++){
        let inp = inputs[i];
        let msg = messages[i];
        if (!inp.value) {
            // inp.classList.add("alert-danger")
            msg.classList.remove("d-none");
            flag = false;
        }else{
            // inp.classList.remove("alert-danger");
            msg.classList.add("d-none");
        }
    }
    return flag;
}