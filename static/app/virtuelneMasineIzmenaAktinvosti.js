let virtuelneMasineIzmenaAktinvosti = new Vue({

    el:"#virtuelneMasineIzmenaAktinvosti",
    data : {
        staraAktivnost : "",
        aktivnosti : [],
        virtuelnaMasina: "",
        pocetak : "",
        zavrsetak : "",
    },

    mounted () {

        this.parsePatams()
    },


    methods : {
        parsePatams:function(){
            let uri = window.location.search.substring(1);
            let params = new URLSearchParams(uri);

            this.init(params.get("id"),params.get("pocetak"))
        },
        init:function(id, pocetak){
            axios.get('/virtuelneMasine/'+id).then(response => {
                this.virtuelnaMasina = response.data;
                this.aktivnosti = this.virtuelnaMasina.aktivnosti;
                console.log(pocetak);
                for(a of this.aktivnosti){
                    console.log(a.pocetak);
                    if(a.pocetak==pocetak){
                        this.staraAktivnost = a;
                        break;
                    }
                }
                console.log(this.aktivnost)
            });
        },
        izmenaAktivnost:function(){
            console.log("AAAAAA");
            if(this.pocetak=="" && this.zavrsetak=="" ){
                new Toast({
                    message:"Popuni pravilno makar jedno od polja za izmenu,",
                    type: 'danger'
                });
            }else {
                novaAktivnost = {
                    pocetak: this.pocetak,
                    zavrsetak: this.zavrsetak
                };
                axios.put("/virtuelneMasine/" + this.virtuelnaMasina.id + "/" + this.staraAktivnost.pocetak, novaAktivnost).then(response => {
                    window.location.href = "/virtuelneMasineAktinvosti.html?id=" + this.virtuelnaMasina.id;

                }).catch(error => {
                    let msg = error.response.data.ErrorMessage;
                    new Toast({
                        message: msg,
                        type: 'danger'
                    });
                });
            }
        },
    }
});

