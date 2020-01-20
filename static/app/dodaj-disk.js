let loginapp = new Vue({
    el:"#dodajdiskapp",
    data: {
       ime: null,
       tip: null,
       kapacitet: null,
       vm: null,
       vmasine: null,
    },
    mounted() {
        axios.get('/virtuelneMasine').then(response => {
            this.vmasine = response.data;
        });  
    },
    
    methods:{
        checkParams: checkFormParams
        ,
        dodajDisk:function() {
            if(!this.checkParams()){
                return;
            }
            let vmasina = axios.get("/virtualneMasine/"+this.vm);
            if(!vmasina){
                vmasina = {id:null};
            }
            let promise = axios.post("/diskovi",{
                ime: this.ime,
                tip: this.tip,
                kapacitet: this.kapacitet,
                vm:vmasina
              }
            )
            promise.then(response=>{
                    
                    if (response.status) {
                        window.location.replace("/diskovi.html");
                    }else{
                        new Toast({
                            message:response.statusText,
                            type: 'danger'
                        });
                    }

            }).catch(error=>{
                new Toast({
                    message:error.response.data.ErrorMessage,
                    type: 'danger'
                });
            });
        }
    }
});



