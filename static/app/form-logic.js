checkFormParams = function(){
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