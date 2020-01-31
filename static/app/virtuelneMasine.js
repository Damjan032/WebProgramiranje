Vue.use(VueRouter);
const virtuelneMasine = { template: '<virtuelne-masine></virtuelne-masine>' }
const detaljiVM = { template: '<detalji-vm></detalji-vm>' }
const dodajVM = { template: '<dodaj-vm></dodaj-vm>' }
const virtuelneMasineAktinvosti  = { template: '<vm-aktivnosti></vm-aktivnosti>' }
const router = new VueRouter({
    routes: [
        { path: '/',
            component: virtuelneMasine
        },
        { path: '/dodajVM',
            component: dodajVM
        },
        { path: '/aktinvosti/:id',
            component: virtuelneMasineAktinvosti
        },
        { path: '/detaljiVM/:vm/:tipKorisnika',
            name:"detaljiVM",
            component: detaljiVM,
            props: true
        }
    ]
});

let vmapp = new Vue({
    router,
    el:"#vmApp",
    data: {
    },
    mounted() {
    },
    methods:{

    }
});