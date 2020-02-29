Vue.component("racuni",{

    data: function () {
        return {
            racuni : null,
            pocetniDatum : null,
            krajnjiDatum : null,
            intervalniRacun:null
        }
    }, 
    mounted () {
       axios.get("/racuni").then(res=>{
           this.racuni = res.data;
       }).catch(error=> {
        let msg = error.response.data.ErrorMessage;
        new Toast({
            message: msg,
            type: 'danger'
        });
    });
        

    },
    methods:{
        pretrazi:function () {
            if(!this.pocetniDatum||!this.krajnjiDatum){
                new Toast({
                    message: 'Niste uneli datume',
                    type: 'danger'
                });
                return;
            }
            axios.get("/racuni/"+this.pocetniDatum+"/"+this.krajnjiDatum)
            .then(response=>{
                this.intervalniRacun = response.data;
            })
            .catch(error=> {
                let msg = error.response.data.ErrorMessage;
                new Toast({
                    message: msg,
                    type: 'danger'
                });
            });
        }
    },
    template:
`
        <div>
            <div class="container">
                <div class="row">
                    <div class="col">
                        <h3>
                            Mesečni računi
                        </h3>
                        <table class="table">
                            <thead class="thead-dark">
                            <tr>
                                <th>
                                    Organizacija
                                </th>
                                
                                <th>
                                    Početak
                                </th>
                                <th>
                                    Kraj
                                </th>
                                <th>
                                    Cena
                                </th>
                            </tr>
                            </thead>
                            <tr v-for="r in racuni">
                                <td>
                                    {{r.org.ime}}
                                </td>
                                <td>
                                    <template v-if="r.pocetak">

                                        {{r.pocetak}}
                                    </template>
                                </td>
                                <td>
                                    <template v-if="r.kraj">
                                        {{r.kraj.day}}-{{r.kraj.month}}-{{r.kraj.year}}
                                    </template>
                                </td>
                                <td>
                                    {{r.cena.toFixed(2)}}
                                </td>
                            </tr>
                        </table>
                    </div>
                    <div class="col">
                        <h3>
                            Cena za određeni period
                        </h3>
                        <table>
                            <tr>
                                
                                <th>
                                    Početni datum
                                </th>
                                <th>
                                    Krajnji datum
                                </th>
                            </tr>
                            <tr>
                                <td>
                                    <input type="date" v-model="pocetniDatum">

                                </td>
                                <td>
                                    <input type="date" v-model="krajnjiDatum">
                                </td>
                                <td>
                                    <button @click= "pretrazi()" type="button" class="btn btn-success">Pretraži</button>
                                </td>
                            </tr>

                        </table>
                        <table class="table" v-if="intervalniRacun">
                            <thead class="thead-dark">
                                <tr>
                                    <th>
                                        Ime
                                    </th>
                                    
                                    <th>
                                        Tip
                                    </th>
                                    <th>
                                        Cena za period
                                    </th>
                                </tr>
                            </thead>
                            <tbody>

                                <tr v-for="racun in intervalniRacun.resursRacuni">
                                    <td>
                                        {{racun.imeResursa}}
                                    </td>
                                    <td>
                                        {{racun.tipResursa}}
                                    </td>
                                    <td>
                                        {{racun.cena.toFixed(2)}}
                                    </td>
                                </tr>
                            </tbody>
                            <thead class="thead-light">
                                <tr>
                                    <th></th>
                                    <tH>Ukupna cena: </tH>
                                    <th>{{intervalniRacun.ukupnaCena.toFixed(2)}}</th>
                                </tr>
                            </thead> 
                        </table>
                    </div>
                </div>
            </div>
        </div>
 
`
});

