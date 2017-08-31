using System.Collections;
using System.Collections.Generic;
using UnityEngine;
using UnityEngine.UI;

public class LoadScreen : MonoBehaviour {

    [SerializeField]
    private float units;

    [SerializeField]
    private GameObject createPrefab;

    [SerializeField]
    private Image fill;

    private float fillAmount;

    [SerializeField]
    private GameObject button;

	void Start ()
    {
        StartCoroutine(BuildUnits());
	}
	
	void Update ()
    {
        UpdateBar();
	}

    public IEnumerator BuildUnits()
    {
        Vector2 topLeft = Camera.main.ViewportToWorldPoint(new Vector2(0, 1));
        Vector2 bottomRight = Camera.main.ViewportToWorldPoint(new Vector2(1, 0));

        for (int i = 0; i <= units; i++)
        {
            Vector2 randomPos = new Vector2(Random.Range(topLeft.x, bottomRight.x), Random.Range(topLeft.y, bottomRight.y));

            fillAmount = i / units;

            Instantiate(createPrefab, randomPos, Quaternion.identity);
            yield return null;
        }

        // 로딩이 완료되었을 때!

        button.SetActive(true);
    }

    private void UpdateBar()
    {
        fill.fillAmount = fillAmount;
    }

    public void StartGame()
    {
        gameObject.SetActive(false);
    }
}
