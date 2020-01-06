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
          /*  axios.delete('/organizacije/'+id).then(response => {
                      window.location.reload();
            });*/
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