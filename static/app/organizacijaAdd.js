let organizacijaApp = new Vue({
    el:"#orgAdd",
    data : {
        oIme : "",
        oOpis : "",
        slika : ""
    },

    methods : {

        checkParams: function(){
            let inputs = document.getElementsByTagName("input");
            let messages = document.getElementsByClassName("alert");
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
        },

        onFileSelected(event){
            this.slika = event.target.files[0];
        },

        addOrganizaciju:function(ime, opis){
            if(!this.checkParams()){
                return;
            }
            // console.log(this.slika.name);
            let data = new FormData();
            data.append("nazivSlike", this.slika.name)
            data.append('oIme', ime);
            data.append('oOpis', opis);
            data.append('oSlika',this.slika);
            let promise = axios.post('/organizacije', data, { headers: {'Content-Type': 'multipart/form-data'}});

            promise.then(response=>{
                if (response.data.status) {
                    window.location.replace("/vmpregled.html");
                }

            }).catch(error=>{
                let msg = error.response.data.ErrorMessage;
                new Toast({
                    message:msg,
                    type: 'danger'
                });
            });
        }

    }
});

