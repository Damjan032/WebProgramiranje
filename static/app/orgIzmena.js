let orgIzmena = new Vue({
    el:"#organizacije",
    data: {
        organizacije : null,
        korisnikType : null
    },
    mounted () {
        axios.get('/organizacije').then(response => {
            this.organizacije = response.data;
            console.log(this.organizacije);
        });
        axios.get('/getUserType').then(response => {
            this.korisnikType = response.data;
        });
    },
    methods:{
        obrisi:function(id){
            console.log(id);
            axios.delete('/organizacije/'+id).then(response => {
                 window.location.reload()
            });
        }
    }
});