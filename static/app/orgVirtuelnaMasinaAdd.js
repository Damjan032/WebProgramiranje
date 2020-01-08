let orgVirtuelnaMasinaAdd = new Vue({
    el:"#orgVirtuelnaMasinaAdd",
    data: {
        organizacija : "",
         virtuelneMasine : "",
         izabranaVM : ""
    },
    mounted () {
        let uri = window.location.search.substring(1);
        let params = new URLSearchParams(uri);
        console.log(params.get("id"));
        axios.get('/virtuelneMasine').then(response => {
             this.virtuelneMasine = response.data;
             console.log(this.virtuelneMasine);
        });
        axios.get('/organizacije/'+params.get("id")).then(response => {
            this.organizacija = response.data;
            console.log(this.organizacija);
        });

    },
    methods:{
        dodajVM:function(){
             console.log(this.izabranaVM);
             if(this.izabranaVM==""){
                                      new Toast({
                                          message:"Izaberi viruelnu masinu",
                                          type: 'danger'
                                      });
             }
             else{
                axios.put('/organizacije/'+this.organizacija.id+'/vm/'+this.izabranaVM).then(response => {
                             window.location.href="/orgResursi.html?id="+this.organizacija.id;

                }).catch(error=>{
                      let msg = error.response.data.ErrorMessage;
                      new Toast({
                          message:msg,
                          type: 'danger'
                      });
                 });
             }
        },
    }
});