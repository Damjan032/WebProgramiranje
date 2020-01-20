let virtuelneMasineAktivnosti = new Vue({

    el:"#virtuelneMasineAktivnosti",
    data : {
        aktivnosti : [],
        virtuelnaMasina: "",    vreme:""
    },

    mounted () {
            let uri = window.location.search.substring(1);
            let params = new URLSearchParams(uri);
            console.log(params.get("id"));
            this.init(params.get("id"))

     },


    methods : {
        init:function(id){
            axios.get('/virtuelneMasine/'+id).then(response => {
                this.virtuelnaMasina = response.data;
                console.log(this.virtuelnaMasina)
                this.aktivnosti = this.virtuelnaMasina.aktivnosti;
                console.log(this.aktivnosti)
            });
        },
        izmenaAktivnosti:function(a){
            console.log(a.pocetak);
            window.location.href="/virtuelneMasineIzmenaAktinvosti.html?id="+this.virtuelnaMasina.id+"&pocetak="+a.pocetak;
        },
        brisanjeAktivnosti:function(a){
            axios.delete("/virtuelneMasine/"+this.virtuelnaMasina.id+"/"+a.pocetak).then(response =>{
                this.init(this.virtuelnaMasina.id);
                new Toast({
                    message:"Uspesno obrisana aktivnost",
                    type: 'success'
                });
            })
        },
    }
});

