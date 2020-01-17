let vmKatPregled = new Vue({
    el:"#vmKatPregled",
    data: {
        kategorije : null,
    },
    mounted () {
        axios.get('/vmKategorije').then(response => {
            this.kategorije = response.data;
            console.log(this.kategorije);
        });
    },
    methods:{
        obrisi:function(id){
            axios.delete('/vmKategorije/'+id).then(response => {
                      window.location.reload();
            }).catch(error=>{
                let msg = error.response.data.ErrorMessage;
                new Toast({
                    message:msg,
                    type: 'danger'
                });
            });
        },
        pregledKorisnika:function(id){
            /*window.location.href="/orgKorisnici.html?id="+id;*/
        },

        izmeni:function(id){
            window.location.href="/vmKatIzmena.html?id="+id;
        },

        pregledResursa:function(id){
           /* window.location.href="/orgResursi.html?id="+id;*/
        }
    }
});