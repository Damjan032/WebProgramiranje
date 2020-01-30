Vue.component("vm-aktivnosti",{
    data:function () {
        return{
            virtuelneMasine : null,
            kategorijeBrJezgara : [],
            kategorijeRAM : [],
            kategorijeGPU : [],
            korisnikType : null,
            naziv : ""
        }
    },
    mounted:function () {
        let uri = window.location.search.substring(1);
        let params = new URLSearchParams(uri);
        console.log(params.get("id"));
        this.init(params.get("id"))
    },
    methods:{
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
    },
    template:`<div class="container">
        <div class="page-header">
            <h2>Pregled aktivnosti virtuelnih masine {{virtuelnaMasina.ime}}</h2>
        </div>
        <br><br>

        <br><br>
        <p>
            <table class="table">
                <tr>
                    <th>
                        Pocetak aktivnosti
                    </th>
                    <th>
                        Kraj aktivnosti
                    </th>

                    <th>
                       Izmeni aktivnost
                    </th>
                </tr>

                <tr v-for = "a in aktivnosti" >
                    <td>
                         <p>{{a.pocetak}}</p>
                    </td>
                    <td text-align="center">
                        {{a.zavrsetak}}
                    </td>
                    <td text-align="center">
                       <button type="button" class="btn btn-secondary" v-if="a.zavrsetak!=null" @click= "izmenaAktivnosti(a)" >Izmeni aktivnost</button>
                    </td>
                    <td text-align="center">
                        <button type="button" class="btn btn-secondary" v-if="a.zavrsetak!=null" @click= "brisanjeAktivnosti(a)" >Obrisi aktivnost</button>
                    </td>
                </tr>
        </table>
    </div>
    `


});