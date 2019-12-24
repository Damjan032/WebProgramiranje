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
            console.log(this.slika);
            // let slika =document.getElementsByClassName("slika");
            const fd = new FormData();

            fd.append('image', this.slika, this.slika.name);
            console.log(this.fd);
            let promise = axios.get('/orgAddSubmit', {params: {oSlika :fd, oIme : ime, oOpis : opis }})

            promise.then(response=>{
                console.log(response);
                new Toast({
                    message:response.data.poruka,
                    type: 'danger'
                });
                if (response.data.status) {
                    window.location.replace("/vmpregled.html");
                }

            });
        }

    }
});

