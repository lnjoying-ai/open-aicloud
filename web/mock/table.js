const Mock = require('mockjs')

const data = Mock.mock({
  'items|30': [{
    id: '@id',
    title: '@sentence(10, 20)',
    'status|1': ['published', 'draft', 'deleted'],
    author: 'name',
    display_time: '@datetime',
    pageviews: '@integer(300, 5000)'
  }]
})

const chartdata = [
  {
    'name': 'mac os',
    'value': 100,
    'children': [
      {
        'name': '版本11.2.1',
        'value': 45,
        'children': [
          {
            'name': 'intel core i7-23222',
            'value': 20
          },
          {
            'name': 'intel core i5-22142',
            'value': 25
          }
        ]
      },
      {
        'name': '版本12.4.1',
        'value': 55,
        'children': [{
          'name': 'intel core i7-23222',
          'value': 40
        },
        {
          'name': 'intel core i5-22142',
          'value': 15
        }
        ]
      }
    ]
  },
  {
    'name': 'windows',
    'value': 200,
    'children': [{
      'name': '10.0.19024',
      'value': 100,
      'children': [{
        'name': 'intel core i7-23222',
        'value': 20
      },
      {
        'name': 'intel core i5-22142',
        'value': 80
      }
      ]
    },
    {
      'name': '10.0.19033',
      'value': 100,
      'children': [{
        'name': 'intel core i7-23222',
        'value': 40
      },
      {
        'name': 'intel core i5-22142',
        'value': 60
      }
      ]
    }
    ]
  }
]

module.exports = [
  {
    url: '/vue-admin-template/table/list',
    type: 'get',
    response: config => {
      const items = data.items
      return {
        code: 20000,
        data: {
          total: items.length,
          items: items
        }
      }
    }
  },
  {
    url: '/vue-admin-template/chart/list',
    type: 'get',
    response: config => {
      return {
        code: 20000,
        data: chartdata
      }
    }
  }
]
