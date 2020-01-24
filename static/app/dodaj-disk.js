let loginapp = new Vue({
    el:"#dodajdiskapp",
    data: {
       ime: null,
       tip: null,
       kapacitet: null,
       vm: null,
       org: null,
       vmasine: null,
       organizacije: null
    },
    mounted() {
        axios.get('/virtuelneMasine').then(response => {
            this.vmasine = response.data;
        }).catch(error=>{
            let msg = error.response.data.ErrorMessage;
            new Toast({
                message:msg,
                type: 'danger'
            });
        }); 
        axios.get('/organizacije').then(response => {
            this.organizacije = response.data;
        }).catch(error=>{
            let msg = error.response.data.ErrorMessage;
            new Toast({
                message:msg,
                type: 'danger'
            });
        });  
    },
    
    methods:{
        checkParams: checkFormParams
        ,
        dodajDisk:function() {
            if(!this.checkParams()){
                return;
            }
            if(!this.vm){
                this.vm = {id:null};
            }else{
                
            }
            let promise = axios.post("/diskovi",{
                disk:{
                    ime: this.ime,
                    tip: this.tip,
                    kapacitet: this.kapacitet,
                    vm:this.vm
                },
                org:this.org

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



